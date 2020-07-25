package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class FarmedComputation {

	public static void main(String args[]) {

		MPP mpp = new MPP();
		// int size = (int)Math.pow(2, 12);
		int size = 100;
		List<List<Integer>> chunkedInput = generateChunkedInput(size);
		List<Integer> sequentialInput = generate(size);
		// List<Integer> out;

		List<Double> result = new ArrayList<>();

		long startTime;
		long endTime;
		// create first pipeline skeleton
		Operation o1 = new Operation1();
	
		Skeleton<List<Integer>, List<Double>> computation = new SequentialOpSkeleton(o1, ArrayList.class);		

		Skeleton<List<List<Integer>>, List<Double>> skel1 = new FarmSkeleton(computation, 4, ArrayList.class);

		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		startTime = System.currentTimeMillis();
		for (int i : sequentialInput) {
			double o = 1 / i;
			result.add(o);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));

		
		startTime = System.currentTimeMillis();
		Future<List<Double>> outputFuture = skel1.submitData(chunkedInput);
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
		int chunkSize = 10;
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
