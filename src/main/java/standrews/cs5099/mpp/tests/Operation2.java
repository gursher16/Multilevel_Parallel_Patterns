package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation2 implements Operation<Double, Double> {

	@Override
	public Double execute(Double inputParam) throws Exception {
		/**
		List<Integer> tempList = new ArrayList<>();
		for (Integer i : inputParam) {
			i = i * 10;
			tempList.add(i);
		}
		**/
		//inputParam = null;
		//inputParam = inputParam * 2.12345;
		System.out.println("INPUT_PARAM OP2: " + inputParam);
		return inputParam;
	}

}
