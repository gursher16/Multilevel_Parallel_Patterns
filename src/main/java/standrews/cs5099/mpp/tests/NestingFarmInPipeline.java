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

public class NestingFarmInPipeline   {

	public static void main(String args[]) {

		MPP mpp = new MPP();
		// int size = (int)Math.pow(2, 12);
		int size = 10000;
		List<List<Integer>> chunkedInput = generateChunkedInput(size);
		List<Integer> sequentialInput = generate(size);
		// List<Integer> out;

		List<Double> result = new ArrayList<>();

		long startTime;
		long endTime;
			
		/* -- NESTING FARM --*/
		Operation o1 = new PipelineOperation1();
		Operation o2 = new FarmOperationNested();		
		Skeleton firstStage = new SequentialOpSkeleton<Integer, Integer>(o1, Integer.class);		 
		Skeleton secondStage = new SequentialOpSkeleton<Integer, Integer>(o2, Integer.class);
		// Farm the second Stage
		Skeleton<Integer, Double> farm = new FarmSkeleton(secondStage, 2, ArrayList.class);		
		Skeleton thirdStage = new SequentialOpSkeleton<Integer, Integer>(o1, Integer.class);
		
		Skeleton stages[] = {firstStage, farm, thirdStage};
		Skeleton pipe = new PipelineSkeleton(stages, ArrayList.class);		
		
		

		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		
		startTime = System.currentTimeMillis();
		for(int i : sequentialInput) {
			// stage 1
			fib(i);
			//stage 2
			fib(i);
			//stage 3
			fib(i);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));		
		
		/*
		startTime = System.currentTimeMillis();
		for (int i : sequentialInput) {
			i = i * 10;
			i = i + 111;
			double o = i * 10;
			//o = i * 5;
			result.add(o);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));
		*/
		
		startTime = System.currentTimeMillis();
		Future<List<Double>> outputFuture = pipe.submitData(sequentialInput);
		try {
			result = outputFuture.get();
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));
			if (result.size() == sequentialInput.size()) {
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


		for (int i = 0; i < size; i++) {
			input.add(i, 25);
		}

		return input;
	}

	public static List<List<Integer>> generateChunkedInput(int size) {

		Random random = new Random();
		List<List<Integer>> chunkedList = new ArrayList<>();
		List<Integer> subList;
		int chunkSize = 2;
		int chunks = size / chunkSize;
		
		for (int i = 0; i < chunks; i++) {
			subList = new ArrayList<>();
			for (int j = 0; j < chunkSize; j++) {
				subList.add(random.nextInt());
			}
			chunkedList.add(subList);
		}
		return chunkedList;
	}
	
	public static int fib(int n) {
		if(n<=1) {
			return n;
		}
		return (fib(n-1) + fib(n-2));
	}

}
