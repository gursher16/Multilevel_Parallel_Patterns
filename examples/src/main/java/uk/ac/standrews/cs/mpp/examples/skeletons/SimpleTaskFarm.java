package uk.ac.standrews.cs.mpp.examples.skeletons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.examples.util.FarmOperation1;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.FarmSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

public class SimpleTaskFarm {

	public static void main(String args[]) {

		System.out.println("Started: " + SimpleTaskFarm.class.getSimpleName());

		MPPSkelLib mpp = null;
		int numCores = 0;
		int inputSize = 0;
		int chunkSize = 0;
		int farmWorkersSize = 0;
		if (args.length == 4) {
			numCores = Integer.parseInt(args[0]);
			inputSize = Integer.parseInt(args[1]);
			chunkSize = Integer.parseInt(args[2]);
			farmWorkersSize = Integer.parseInt(args[3]);
			System.out.println("inputSize: " + inputSize);
			System.out.println("chunkSize: " + chunkSize);

			if (numCores <= Runtime.getRuntime().availableProcessors() && numCores >= 2) {
				mpp = new MPPSkelLib(numCores);
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

		int size = inputSize;
		List<List<Integer>> chunkedInput = generateChunkedInput(size, chunkSize);
		List<Integer> sequentialInput = generate(size);
		Object result;
		List<Double> resultList = new ArrayList<>();

		long startTime;
		long endTime;
		Operation o1 = new FarmOperation1();
		Skeleton<List<Integer>, List<Double>> computation = new SequentialOpSkeleton(o1, ArrayList.class);
		Skeleton<List<List<Integer>>, List<Double>> farm = new FarmSkeleton(computation, farmWorkersSize, ArrayList.class);

		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		System.out.println("Executing Sequential Operation..");
		startTime = System.currentTimeMillis();
		for (int i : sequentialInput) {
			// fib(i);
			fib(i);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));
		
		//////////////////////PARALLEL OPERATION ///////////////////////
		
		System.out.println("Executing Parallel Operation..");
		startTime = System.currentTimeMillis();
		Future<List<Double>> outputFuture = farm.submitData(chunkedInput);
		try {
			result = outputFuture.get();
			if (result instanceof Exception) {
				((Exception) result).printStackTrace();
			} else {
				resultList = (List) result;
				if (resultList.size() == chunkedInput.size()) {
					System.out.println("***SAME SIZE***");
				} else {
					System.out.println("***INCORRECT***");
				}
			}
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			mpp.shutDown();
		}
		System.out.println("FINISHED");

	}

	public static List<Integer> generate(int size) {

		Random random = new Random();
		ArrayList<Integer> input = new ArrayList<Integer>();

		/*
		 * array.add(738); array.add(591); array.add(129); array.add(577);
		 * array.add(276); array.add(913); array.add(15); array.add(170);
		 * array.add(417); array.add(26);
		 */

		for (int i = 0; i < size; i++) {
			input.add(i, 26);
		}

		return input;
	}

	public static List<List<Integer>> generateChunkedInput(int size, int chunkSize) {

		Random random = new Random();
		List<List<Integer>> chunkedList = new ArrayList<>();
		List<Integer> subList;
		int chunks = size / chunkSize;
		System.out.println("number of chunks: " + chunks);
		for (int i = 0; i < chunks; i++) {
			subList = new ArrayList<>();
			for (int j = 0; j < chunkSize; j++) {
				subList.add(26);
			}
			chunkedList.add(subList);
		}
		return chunkedList;
	}

	public static int fib(int n) {
		if (n <= 1) {
			return n;
		}
		return (fib(n - 1) + fib(n - 2));
	}

}
