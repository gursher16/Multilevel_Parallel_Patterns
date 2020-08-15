package standrews.cs5099.mpp.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPPSkelLib;
import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class SimpleTaskFarm {

	public static void main(String args[]) {

		MPPSkelLib mpp = new MPPSkelLib();
		// int size = (int)Math.pow(2, 12);
		int size = 10000000;
		List<List<Integer>> chunkedInput = generateChunkedInput(size);
		List<Integer> sequentialInput = generate(size);
		List<Double> result = new ArrayList<>();

		long startTime;
		long endTime;
	
		
		Operation o1 = new FarmOperation1();
		Skeleton<List<Integer>, List<Double>> computation = new SequentialOpSkeleton(o1, ArrayList.class);		
		Skeleton<List<List<Integer>>, List<Double>> farm = new FarmSkeleton(computation, 2, ArrayList.class);
		
		
				

		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		startTime = System.currentTimeMillis();
		for (int i : sequentialInput) {
			double o = 1/i;
			o = o * 0.5;
			o = o / 1.23;
			//o = i * 5;
			result.add(o);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));
		///////////////////////////////////////////////////////////////////
		
		
		////////////////////////// FARMED OPERATION ///////////////////////
		startTime = System.currentTimeMillis();
		Future<List<Double>> outputFuture = farm.submitData(chunkedInput);
		try {
			result = outputFuture.get();
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));
			if (result.size() == chunkedInput.size()) {
				System.out.println("Same size!!");
			} else {
				System.out.println("Different Size!!");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
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
			input.add(i, random.nextInt());
		}

		return input;
	}

	public static List<List<Integer>> generateChunkedInput(int size) {

		Random random = new Random();
		List<List<Integer>> chunkedList = new ArrayList<>();
		List<Integer> subList;
		int chunkSize = 100000;
		int chunks = size / chunkSize;
		/*
		 * array.add(738); array.add(591); array.add(129); array.add(577);
		 * array.add(276); array.add(913); array.add(15); array.add(170);
		 * array.add(417); array.add(26);
		 */
		for (int i = 0; i < chunks; i++) {
			subList = new ArrayList<>();
			for (int j = 0; j < chunkSize; j++) {
				subList.add(random.nextInt());
			}
			chunkedList.add(subList);
		}
		return chunkedList;
	}

}
