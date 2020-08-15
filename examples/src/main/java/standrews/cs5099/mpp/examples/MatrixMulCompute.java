package standrews.cs5099.mpp.examples;

public class MatrixMulCompute {
	double[] targetRow;
	double[][] targetArray;
	int rowNum;
	double[] resultRow;
	
	public MatrixMulCompute(double[]targetRow, double[][] targetArray, int rowNum) {
		this.targetRow = targetRow;
		this.targetArray = targetArray;
		this.rowNum = rowNum;		
	}
}
