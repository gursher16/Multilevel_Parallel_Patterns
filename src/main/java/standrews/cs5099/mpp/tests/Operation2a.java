package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation2a implements Operation<List<Integer>, Double[]>{
	@Override
	public Double[] execute(List<Integer>inputParam) throws Exception {
		Double[] result = new Double[inputParam.size()];
		for(int i : inputParam) {
			double o = 1/i;
			o = o*0.5;
			o = o/1.23;
			result[i] = o;
		}
		return result;
	}
}
