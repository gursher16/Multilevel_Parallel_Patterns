package standrews.cs5099.mpp.tests;

import standrews.cs5099.mpp.operations.Operation;

public class MatrixMulStage3 implements Operation<MatrixMulCompute, Object> {

	private double[][] result;
	boolean isResultReady;
	int count;
	 public MatrixMulStage3(int row, int col) {
		result = new double[row][col];
		isResultReady = false;
		count=0;
	}
	@Override
	public Object execute(MatrixMulCompute inputParam) throws Exception {
		// retrieve target array and row
		count++;
		if(count==result.length) {
			isResultReady = true;
		}
		double[] resultRow= inputParam.resultRow;
		int rowNum = inputParam.rowNum;
		// result of computation
		result[rowNum] = resultRow;
		if(isResultReady) {
			return null;
		}
		return result;
	}

}
