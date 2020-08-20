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

public class MPPDistFarmWithSerialization {

	public static void main(String args[]) {

		////////////////////////// MPP INIT////////////////////////////////

		MppDistLib mppDist = new MppDistLib(args);
		double startTime = 0;
		int rank = mppDist.getRankOfCurrentProcess();
		int size = mppDist.getNumberOfProcesses();
		int workerInputSize = 10000000;
		// 20 million values to be split amongst both workers
		List<Integer> globalInput = new ArrayList<>(workerInputSize * size);
		// create array for input
		Object[] globalInputArr = new Object[workerInputSize * size];

		// Master worker generates input values
		if (rank == 0) {
			globalInput = generate(workerInputSize * size);
			startTime = System.currentTimeMillis();
			globalInputArr = globalInput.toArray();

		}

		// Wait till master worker generates input
		mppDist.mpiBarrier();

		// Buffer which contains input values for each worker
		Object[] inputBufArr = new Object[workerInputSize];

		// Scatter input to all workers
		System.out.println("Process: " + rank + " invoked scatter");		
		mppDist.sendToAllProcesses(globalInputArr, inputBufArr.length, Object.class, inputBufArr, inputBufArr.length,
				Object.class, 0);

		// Deserialise input
		List<Object> inputBuf = Arrays.asList(inputBufArr);

		// Skeleton library computation
		List<Object> result = mppRun(inputBuf, rank);
		Object[] resultArr = result.toArray();
		Object[] finalResult = new Object[workerInputSize * size];
		
		mppDist.receiveFromAllProcesses(resultArr, resultArr.length, Object.class, finalResult, resultArr.length,
				Object.class, 0);
		
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

	public static List<Object> mppRun(List<Object> inputBuf, int rank) {
		////////////////////////// MPP ////////////////////////////////////

		// Initialise MPP library
		MPPSkelLib mpp = new MPPSkelLib();
		List<Object> result = new ArrayList<>();
		List<Object> workerInput = inputBuf;
		System.out.println("Process: " + rank + " input size: " + workerInput.size());
		List<List<Object>> chunkedInput = generateChunkedInput(workerInput);
		// System.out.println("Chunked Input Size = " + chunkedInput.size());
		long startTime;
		long endTime;

		Operation o1 = new FarmOperation1();
		Skeleton<List<Object>, List<Object>> computation = new SequentialOpSkeleton(o1, ArrayList.class);
		Skeleton<List<List<Object>>, List<Object>> farm = new FarmSkeleton(computation, 2, ArrayList.class);

		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		/*
		 * startTime = System.currentTimeMillis(); for (int i : sequentialInput) {
		 * double o = 1 / i; o = o * 0.5; o = o / 1.23; //o = i * 5; result.add(o); }
		 * endTime = System.currentTimeMillis();
		 * System.out.println("Sequential Execution time Taken: " + (endTime -
		 * startTime));
		 */
		///////////////////////////////////////////////////////////////////

		////////////////////////// FARMED OPERATION ///////////////////////
		startTime = System.currentTimeMillis();
		Future<List<Object>> outputFuture = farm.submitData(chunkedInput);
		try {
			result = outputFuture.get();
			// System.out.println("Result size: " + result.size());
			/*
			 * endTime = System.currentTimeMillis();
			 * System.out.println("Thread Execution time Taken: " + (endTime - startTime));
			 */

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mpp.shutDown();
		}
		/**
		 * double[] resultD = new double[inputBuf.length]; List<Double> finalR = new
		 * ArrayList<Double>(); for (List<Double> list : result) { finalR.addAll(list);
		 * } resultD = finalR.stream().mapToDouble(d -> d).toArray();
		 **/// very expensive computation
		endTime = System.currentTimeMillis();
		System.out.println("Thread Execution time Taken: " + (endTime - startTime));
		System.out.println("FINISHED");
		return result;
	}

	public static List<Integer> generate(int size) {

		Random random = new Random();
		List<Integer> input = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			input.add(random.nextInt());
		}
		return input;
	}

	public static List<List<Object>> generateChunkedInput(List<Object> input) {
		List<List<Object>> chunkedList = new ArrayList<>();
		List<Object> sublist;
		int chunkSize = 100000;
		int chunks = input.size() / chunkSize;
		// System.out.println("Chunks: " + chunks);
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
