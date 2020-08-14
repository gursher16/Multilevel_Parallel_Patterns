package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class MatrixMulStage1 implements Operation<List, List> {

	@Override
	public List<Object> execute(List inputParam) throws Exception {
		// retrieve arrays to be multiplied
		List<Object> computeList = new ArrayList<>();
		double[][] matrix1 = (double[][]) inputParam.get(0);
		double[][] matrix2 = (double[][]) inputParam.get(1);
		// for each row of matrix1 create compute object and add to list
		for (int i = 0; i < matrix1.length; i++) {
			MatrixMulCompute c = new MatrixMulCompute(matrix1[i], matrix2, i);
			computeList.add(c);
		}
		return computeList;
	}

}
