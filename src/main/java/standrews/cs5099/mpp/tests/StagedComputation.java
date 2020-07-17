package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
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

public class StagedComputation {

	public static void main(String args[]) {
		
		
		
		MPP mpp = new MPP();
		//int size = (int)Math.pow(2, 12);
		int size = 1000;
		List<Double> in = generate(size);
		//List<Integer> out;
		
		// create first pipeline skeleton
		Operation o1 = new Operation1();
		Operation o2 = new Operation2();
		Skeleton firstStage = new SequentialOpSkeleton(o1, Double.class);
		Skeleton lastStage = new SequentialOpSkeleton<Double, Double>(o2, Double.class);
		Skeleton skel1 = new PipelineSkeleton(firstStage, lastStage, ArrayList.class);
		
		
		
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
		
			
		Future<ArrayList<Integer>> outputFuture = skel1.submitData(in);
		try {
			outputFuture.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static List<Double> generate(int size){
        
		Random random = new Random();
		ArrayList<Double> array = new ArrayList<Double>();
		/*
		array.add(738);
		array.add(591);
		array.add(129);
		array.add(577);
		array.add(276);
		array.add(913);
		array.add(15);
		array.add(170);
		array.add(417);
		array.add(26);
		*/
		
		for(int i=0;i<size;i++){
			array.add(i, random.nextDouble());
		}

		return array;
	}
	
}
