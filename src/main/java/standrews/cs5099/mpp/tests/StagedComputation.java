package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class StagedComputation {

	public static void main(String args[]) {
		
		
		
		MPP mpp = new MPP();
		//int size = (int)Math.pow(2, 12);
		int size = 10;
		List<Integer> in = generate(size);
		List<Integer> out;
		
		// create first pipeline skeleton
		Operation o1 = new Operation1();
		Operation o2 = new Operation2();
		Skeleton firstStage = new SequentialOpSkeleton<Integer, Integer>(o1);
		Skeleton lastStage = new SequentialOpSkeleton<Integer, Integer>(o2);
		Skeleton skel1 = new PipelineSkeleton<List<Integer>, List<Integer>>(firstStage, lastStage) ;
		
		
		
		// create second pipleine skeleton
		Operation o1a = new Operation1a();
		Operation o2a = new Operation2a();		
		Skeleton firstStagea = new SequentialOpSkeleton<Integer, Integer>(o1a);
		Skeleton lastStagea = new SequentialOpSkeleton<Integer, Integer>(o2a);
		Skeleton skel2 = new PipelineSkeleton<Integer, Integer>(firstStagea, lastStagea) ;
		
		
		/*
		 * Operation o1b = new Operation1b(); Operation o2b = new Operation2b();
		 * Skeleton firstStageb = new SequentialOpSkeleton<List<Integer>,
		 * List<Integer>>(o1b); Skeleton lastStageb = new
		 * SequentialOpSkeleton<List<Integer>, List<Integer>>(o2b);
		 */
		
		// create main pipleine skeleton //
		Skeleton pipeSkel = new PipelineSkeleton<List<Integer>, List<Integer>>(skel1, skel2) ;
		
		
		skel1.submitData(in);
		
	}
	
	public static List<Integer> generate(int size){
        
		Random random = new Random();
		ArrayList<Integer> array = new ArrayList<Integer>(size);
		
		for(int i=0;i<size;i++){
			array.add(i, random.nextInt(1000));
		}

		return array;
	}
	
}
