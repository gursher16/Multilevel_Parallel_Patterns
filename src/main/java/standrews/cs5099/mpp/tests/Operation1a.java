package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation1a implements Operation<Integer, Double> {
	@Override
	public Double execute(Integer inputParam) throws Exception {
		/**
		List<Integer> tempList = new ArrayList<>();
		// usage of streams are possible
		// inputParam.stream().forEach(action);
		for (int i : inputParam) {
			i = i + 5;
			tempList.add(i);
		}
		inputParam = null;**/
		System.out.println("INPUT_PARAM OP3: " + inputParam);
		Double outputParam = (double) (inputParam * 0.5);
		return outputParam;
	}

}
