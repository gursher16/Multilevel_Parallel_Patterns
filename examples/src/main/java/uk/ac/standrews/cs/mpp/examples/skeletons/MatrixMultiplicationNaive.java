package uk.ac.standrews.cs.mpp.examples.skeletons;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.examples.util.MatrixMulStage1;
import uk.ac.standrews.cs.mpp.examples.util.MatrixMulStage2;
import uk.ac.standrews.cs.mpp.examples.util.MatrixMulStage3;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.FarmSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.PipelineSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

public class MatrixMultiplicationNaive {

	public static void main(String args[]) {

		System.out.println("Started: " + MatrixMultiplicationNaive.class.getSimpleName());

		MPPSkelLib mpp = null;
		int rowColSize = 0;
		int numCores = 0;
		int farmWorkerSize = 0;
		if (args.length == 3) {
			numCores = Integer.parseInt(args[0]);
			rowColSize = Integer.parseInt(args[1]);
			farmWorkerSize = Integer.parseInt(args[2]);
			if (numCores <= Runtime.getRuntime().availableProcessors() && numCores >= 2) {
				mpp = new MPPSkelLib(numCores);
			} else {
				System.err.println("Invalid number of processors!");
				System.exit(-2);
			}
			if (rowColSize <= 0) {
				System.err.println("Invalid input size -- should be a positive integer !");
				System.exit(-4);
			}
			if (farmWorkerSize > numCores || farmWorkerSize < 2) {
				System.err
						.println("Invalid farm worker size -- should be less than number of cores and greater than 2!");
				System.exit(-4);
			}

		} else {
			System.err.println("Invalid number of arguments!");
			System.exit(-1);
		}

		/////////////////////// SEQUENTIAL///////////////////////////
		System.out.println("Executing Sequential Operation..");
		double[][] matrix1 = generateMatrix(rowColSize, rowColSize);
		double[][] matrix2 = generateMatrix(rowColSize, rowColSize);
		long startTime;
		long endTime;
		 //displayMatrix(matrix1);
		 //displayMatrix(matrix2);

		double[][] result = new double[rowColSize][rowColSize];
		startTime = System.currentTimeMillis();
		for (int i = 0; i < matrix1.length; i++) {
			for (int j = 0; j < matrix2[0].length; j++) {
				double sum = 0;
				for (int k = 0; k < result.length; k++) {
					sum += matrix1[i][k] * matrix2[k][j];
				}
				result[i][j] = sum;
			}
		}
		//displayMatrix(result);
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Time: " + (endTime - startTime));

		////////////////////// PARALLEL//////////////////////////////

		System.out.println("Executing Parallel Operation..");
		List<double[][]> matrixList = new ArrayList<>();
		matrixList.add(matrix1);
		matrixList.add(matrix2);
		List<List<double[][]>> inputList = new ArrayList<>();
		inputList.add(matrixList);
		Object parResult;
		List<double[][]> parResultMatrix;

		Operation o1 = new MatrixMulStage1();
		Operation o2 = new MatrixMulStage2();
		Operation o3 = new MatrixMulStage3(rowColSize, rowColSize);

		Skeleton stage1 = new SequentialOpSkeleton(o1, null, true);
		Skeleton stage2 = new SequentialOpSkeleton(o2, null);
		Skeleton stage2Farm = new FarmSkeleton<List, List>(stage2, farmWorkerSize, null);
		Skeleton stage3 = new SequentialOpSkeleton(o3, null);
		Skeleton stages[] = { stage1, stage2Farm, stage3 };
		Skeleton pipe = new PipelineSkeleton(stages, ArrayList.class);

		startTime = System.currentTimeMillis();
		Future<List> outputFuture = pipe.submitData(inputList);

		try {
			parResult = outputFuture.get();
			if (parResult instanceof Exception) {
				((Exception) parResult).printStackTrace();
			} else {
				parResultMatrix = (List) parResult;
				//displayMatrix(parResultMatrix.get(0));
			}
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Time: " + (endTime - startTime));
			
		} catch (InterruptedException | ExecutionException e) {
			
			e.printStackTrace();
		} finally {
			mpp.shutDown();
		}

	}

	public static double[][] generateMatrix(int row, int col) {
		Random ran = new Random();
		// initialise matrix
		double[][] matrix = new double[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				matrix[i][j] = ran.nextInt(10);
			}
		}
		return matrix;
	}

	public static void displayMatrix(double[][] matrix) {
		System.out.println("Matrix: ");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}

	}
}
