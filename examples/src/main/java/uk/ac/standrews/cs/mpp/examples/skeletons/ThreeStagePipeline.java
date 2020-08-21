package uk.ac.standrews.cs.mpp.examples.skeletons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.examples.util.PipelineOperation1;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.PipelineSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

public class ThreeStagePipeline {

	public static void main(String args[]) {

		System.out.println("Started: " + ThreeStagePipeline.class.getSimpleName());
		MPPSkelLib mpp = null;
		int inputSize = 0;
		int numCores = 0;
		if (args.length == 2) {
			numCores = Integer.parseInt(args[0]);
			inputSize = Integer.parseInt(args[1]);
			if (numCores <= Runtime.getRuntime().availableProcessors() && numCores >= 2) {
				mpp = new MPPSkelLib(numCores);
			} else {
				System.err.println("Invalid number of processors!");
				System.exit(-2);
			}
		} else {
			System.err.println("Invalid number of arguments!");
			System.exit(-1);
		}

		int size = inputSize;
		List<Integer> in = generate(size);
		Object result;
		List<Integer> resultList;
		long startTime;
		long endTime;

		Operation o1 = new PipelineOperation1();
		Skeleton firstStage = new SequentialOpSkeleton<Integer, Integer>(o1, Integer.class);
		Skeleton secondStage = new SequentialOpSkeleton<Integer, Integer>(o1, Integer.class);
		Skeleton thirdStage = new SequentialOpSkeleton<Integer, Integer>(o1, Integer.class);

		Skeleton stages[] = { firstStage, secondStage, thirdStage };

		Skeleton skel1 = new PipelineSkeleton(stages, ArrayList.class);

		////////////////////////// SEQUENTIAL OPERATION ///////////////////
		System.out.println("Executing Sequentail Operation..");
		startTime = System.currentTimeMillis();
		for (int i : in) {
			// stage 1
			fib(i);
			// stage 2
			fib(i);
			// stage 3
			fib(i);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Execution time Taken: " + (endTime - startTime));

		//////////////////////PARALLEL OPERATION ///////////////////////

		System.out.println("Executing Parallel Operation..");
		startTime = System.currentTimeMillis();
		Future<List<Integer>> outputFuture = skel1.submitData(in);
		try {
			result = outputFuture.get();
			if (result instanceof Exception) {
				((Exception) result).printStackTrace();
			} else {
				resultList = (List) result;
				if (resultList.size() == in.size()) {
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
		ArrayList<Integer> array = new ArrayList<Integer>();

		for (int i = 0; i < size; i++) {
			// array.add(i, random.nextInt());
			array.add(i, 26);
		}

		return array;
	}

	public static int fib(int n) {
		if (n <= 1) {
			return n;
		}
		return (fib(n - 1) + fib(n - 2));
	}

}
