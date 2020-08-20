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

	public static final int ROW_SIZE = 5;
	public static final int COL_SIZE = 5;

	public static void main(String args[]) {

		///////////////////////SEQUENTIAL///////////////////////////

		double[][] matrix1 = generateMatrix(ROW_SIZE, COL_SIZE);
		double[][] matrix2 = generateMatrix(ROW_SIZE, COL_SIZE);
		long startTime;
		long endTime;
		//displayMatrix(matrix1);
		//displayMatrix(matrix2);

		double[][] result = new double[ROW_SIZE][COL_SIZE];
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
		endTime = System.currentTimeMillis();
		System.out.println("Sequential Time: " + (endTime - startTime));
		//System.out.println("Result: ");
		//displayMatrix(result);
		//////////////////////PARALLEL//////////////////////////////
		
		MPPSkelLib mpp= new MPPSkelLib();
		List<double[][]> matrixList = new ArrayList<>();
		matrixList.add(matrix1);
		matrixList.add(matrix2);
		List<List<double[][]>> inputList = new ArrayList<>();
		inputList.add(matrixList);
		Object parResult;
		List<double[][]> parResultMatrix;
		
		Operation o1 = new MatrixMulStage1();
		Operation o2 = new MatrixMulStage2();
		Operation o3 = new MatrixMulStage3(ROW_SIZE, COL_SIZE);
		
		Skeleton stage1= new SequentialOpSkeleton(o1, null, true);
		Skeleton stage2= new SequentialOpSkeleton(o2, null);
		Skeleton stage2Farm = new FarmSkeleton<List, List>(stage2, 4, null);
		Skeleton stage3 = new SequentialOpSkeleton(o3, null);		
		Skeleton stages[] = {stage1,stage2Farm,stage3};
		Skeleton pipe = new PipelineSkeleton(stages, ArrayList.class);
		
		startTime = System.currentTimeMillis();
		Future<List> outputFuture = pipe.submitData(inputList);
		
		try {
			parResult = outputFuture.get();
			if(parResult instanceof Exception) {
				((Exception)parResult).printStackTrace();
			}
			else {
				parResultMatrix = (List)parResult;
			}
			endTime = System.currentTimeMillis();
			System.out.println("Parallel Time: " + (endTime - startTime));
			//displayMatrix(parResult.get(0));
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
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
