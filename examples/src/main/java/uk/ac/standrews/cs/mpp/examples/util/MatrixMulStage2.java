package uk.ac.standrews.cs.mpp.examples.util;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class MatrixMulStage2 implements Operation<MatrixMulCompute, MatrixMulCompute> {

	@Override
	public MatrixMulCompute execute(MatrixMulCompute inputParam) throws Exception {
		// retrieve target array and row
		double[][] targetArray = inputParam.targetArray;
		double[] targetRow= inputParam.targetRow;
		// result of computation
		double[] resultRow = new double[targetRow.length];
		// multiply 
		for(int i=0;i<targetRow.length;i++) {
			double sum = 0;
			for(int j=0;j<targetArray.length; j++) {
				sum+=targetRow[j] * targetArray[j][i];
			}
			resultRow[i] = sum;			
		}		
		inputParam.resultRow = resultRow;
		return inputParam;
	}

}
