package standrews.cs5099.mpp.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class ExceptionHandlingPipeline{

	public static void main(String args[]) {
		
		
		
		MPP mpp = new MPP();
		//int size = (int)Math.pow(2, 12);
		int size = 10000;
		List<Integer> in = generate(size);
		//List<Integer> out;
		Object result;
		List<Integer> resultList = new ArrayList<>();
		
		long startTime;
		long endTime;
		// create first pipeline skeleton
		Operation o1 = new PipelineOperationException();
		Operation o2 = new PipelineOperation2();
		Operation o3 = new PipelineOperation3();
				
		Skeleton firstStage = new SequentialOpSkeleton<Integer, Integer>(o2, Integer.class);
		Skeleton secondStage = new SequentialOpSkeleton<Integer, Integer>(o1, Integer.class);
		Skeleton thirdStage = new SequentialOpSkeleton<Integer, Integer>(o3, Integer.class);
		
		Skeleton stages[] = {firstStage, secondStage, thirdStage};
		
		
		Skeleton skel1 = new PipelineSkeleton(stages, ArrayList.class);
		
		
		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		startTime = System.currentTimeMillis();
		for(int i : in) {
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
		for(int i: in) {
			i = i * 10;
			i = i + 111;
			double o = i * 10;
			result.add(o);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));
		*/
		
		
		//////////////////////////////////////////////////////////////
		
		
		
		// create second pipleine skeleton
		/*
		Operation o1a = new Operation1a();
		Operation o2a = new Operation2a();		
		Skeleton firstStagea = new SequentialOpSkeleton<Integer, Integer>(o1a);
		Skeleton lastStagea = new SequentialOpSkeleton<Integer, Integer>(o2a);
		Skeleton skel2 = new PipelineSkeleton<Integer, Integer>(firstStagea, lastStagea) ;
		*/
		
		/*
		 * Operation o1b = new Operation1b(); Operation o2b = new Operation2b();
		 * Skeleton firstStageb = new SequentialOpSkeleton<List<Integer>,
		 * List<Integer>>(o1b); Skeleton lastStageb = new
		 * SequentialOpSkeleton<List<Integer>, List<Integer>>(o2b);
		 */
		
		// create main pipleine skeleton //
		//Skeleton pipeSkel = new PipelineSkeleton<List<Integer>, List<Integer>>(skel1, skel2) ;
		//List<Integer> outputList;
		
		startTime = System.currentTimeMillis();
		Future<List<Integer>> outputFuture = skel1.submitData(in);
		try {
			result = outputFuture.get();
			if(result instanceof Exception) {
				((Exception)result).printStackTrace();
			}
			else {
				resultList = (List)result;
			}
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Execution time Taken: " + (endTime - startTime));
			if(resultList.size() == in.size()) {
				System.out.println("***SAME SIZE***");
			}
			else {
				System.out.println("***INCORRECT***");
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			mpp.shutDown();
		}
		System.out.println("EXECUTION - FINISHED");
		
	}
	
	public static List<Integer> generate(int size){
        
		Random random = new Random();
		ArrayList<Integer> array = new ArrayList<Integer>();
		
		/*
		array.add(1);
		array.add(2);
		array.add(3);
		array.add(4);
		array.add(5);
		array.add(6);
		array.add(7);
		array.add(8);
		array.add(9);
		array.add(10);
		*/
		
		for(int i=0;i<size;i++){
			//array.add(i, random.nextInt());
			array.add(i,25);
		}

		return array;
	}
	
	public static int fib(int n) {
		if(n<=1) {
			return n;
		}
		return (fib(n-1) + fib(n-2));
	}
	
}
