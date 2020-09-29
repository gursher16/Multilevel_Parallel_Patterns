package uk.ac.standrews.cs.mpp.examples.mpj;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import mpi.MPI;
import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.core.MppDistLib;
import uk.ac.standrews.cs.mpp.examples.util.FarmOperation1;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.FarmSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

public class MPPDistSimpleTaskFarm {

	public static void main(String args[]) {
		
		System.out.println("Started: " + Class.class.getName());
		////////////////////////// MPP INIT////////////////////////////////

		MPPSkelLib mpp = null;
		MppDistLib mppDist = null;
		int numCores = 0;
		int inputSize = 0;
		int chunkSize = 0;
		int farmWorkersSize = 0;
		if (args.length == 7) {
			numCores = Integer.parseInt(args[3]);
			inputSize = Integer.parseInt(args[4]);
			chunkSize = Integer.parseInt(args[5]);
			farmWorkersSize = Integer.parseInt(args[6]);
			System.out.println("inputSize: " + inputSize);
			System.out.println("chunkSize: " + chunkSize);

			if (numCores <= Runtime.getRuntime().availableProcessors() && numCores >= 2) {
				mpp = new MPPSkelLib(numCores);
				mppDist = new MppDistLib(args);
			} else {
				System.err.println("Invalid number of processors!");
				System.exit(-2);
			}
			if (chunkSize > inputSize || inputSize % chunkSize != 0) {
				System.err.println(
						"Invalid chunk size -- inputSize should greater than 0 and be evenly divisible by chunkSize!");
				System.exit(-3);
			}
			if (farmWorkersSize > numCores || farmWorkersSize < 2) {
				System.err
						.println("Invalid farm worker size -- should be less than number of cores and greater than 2!");
				System.exit(-4);
			}
		} else {
			System.err.println("Invalid number of arguments!");
			System.exit(-1);
		}
		
		double startTime = 0;
		int rank = mppDist.getRankOfCurrentProcess();
		int size = mppDist.getNumberOfProcesses();
		int workerInputSize = inputSize;		
		int[] globalInput = new int[workerInputSize * size];
		// Master worker creates the array
		if (rank == 0) {
			globalInput = generate(workerInputSize * size);
			//seqOp(globalInput);
			startTime = System.currentTimeMillis();
		}
		
		// Wait till master worker generates input array
		mppDist.mpiBarrier();		
		//MPI.COMM_WORLD.Barrier();
		
		// Buffer which contains input values for each worker
		int[] inputBuf = new int[workerInputSize];
		
		// Scatter input to all workers
		System.out.println("Process: " + rank + " invoked scatter");
		mppDist.sendToAllProcesses(globalInput, workerInputSize, Integer.class, inputBuf, workerInputSize, Integer.class, 0);
		//MPI.COMM_WORLD.Scatter(globalInput, 0, workerInputSize, MPI.INT, inputBuf, 0, workerInputSize, MPI.INT, 0);
		
		// MPP library to compute
		double[] result = mppRun(inputBuf, rank, chunkSize);
		double[] finalResult = new double[workerInputSize * size];
		
		// Gather results in finalResult
		//MPI.COMM_WORLD.Gather(result, 0, result.length, MPI.DOUBLE, finalResult, 0, result.length, MPI.DOUBLE, 0);
		mppDist.receiveFromAllProcesses(result, result.length, Double.class, finalResult, result.length, Double.class, 0);
		
		if (rank == 0) {
			System.out.println("Final Result Size: " + finalResult.length);
			double endTime = System.currentTimeMillis();
			System.out.println("Total Parallel Execution time Taken: " + (endTime - startTime));
		}

		try {
			mppDist.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static double[] mppRun(int[] inputBuf, int rank, int chunkSize) {
		////////////////////////// MPP ////////////////////////////////////

		// Initialise MPP library
		MPPSkelLib mpp = new MPPSkelLib();
		Object result = null;
		List<Double> resultList = new ArrayList<>();
		// Convert inputBuf to Collection
		List<Integer> workerInput = Arrays.stream(inputBuf).boxed().collect(Collectors.toList());
		System.out.println("Process: " + rank + " input size: " + workerInput.size());
		List<List<Integer>> chunkedInput = generateChunkedInput(workerInput, chunkSize);
		
		long startTime;
		long endTime;

		Operation o1 = new FarmOperation1();
		Skeleton<List<Integer>, List<Double>> computation = new SequentialOpSkeleton(o1, ArrayList.class);
		Skeleton<List<List<Integer>>, List<Double>> farm = new FarmSkeleton(computation, 2, ArrayList.class);

		startTime = System.currentTimeMillis();
		Future<List<Double>> outputFuture = farm.submitData(chunkedInput);
		try {
			result = outputFuture.get();
			if(result instanceof Exception)
			{
				((Exception) result).printStackTrace();
			}
			else {
				resultList = (List) result;
			}
		} catch (InterruptedException | ExecutionException e) {
			//TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mpp.shutDown();
		}

		double[] resultD = new double[inputBuf.length];
		
		//resultD = resultList.stream().mapToDouble(d -> d).toArray();// very expensive computation
		endTime = System.currentTimeMillis();
		System.out.println("Thread Execution time Taken: " + (endTime - startTime));
		System.out.println("FINISHED");
		return resultD;
	}

	public static int[] generate(int size) {

		Random random = new Random();
		int[] input = new int[size];
		for (int i = 0; i < size; i++) {
			input[i] = 26;
		}
		return input;
	}

	public static List<List<Integer>> generateChunkedInput(List<Integer> input, int chunkSize) {
		List<List<Integer>> chunkedList = new ArrayList<>();
		List<Integer> sublist;
		int chunks = input.size() / chunkSize;
		System.out.println("number of chunks: " + chunks);
		int startIndex = 0;
		int chunkCount = 0;
		while (chunkCount != chunks) {
			sublist = input.subList(startIndex, startIndex + chunkSize);
			chunkedList.add(sublist);
			startIndex = startIndex + chunkSize;
			chunkCount += 1;
		}
		return chunkedList;

	}

	public static void seqOp(int[] input) {
		System.out.println("Seq Input Size: " + input.length);
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < input.length; i++) {
			double o = 1 / input[i];
			o = o * 0.5;
			o = o / 1.23;
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));

	}

}
