package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation1 implements Operation<Double, Double> {

	@Override
	public Double execute(Double inputParam) throws Exception {
		/*
		List<Integer> tempList = new ArrayList<>();
		// usage of streams are possible
		// inputParam.stream().forEach(action);
		for (int i : inputParam) {
			i = i % 2;
			tempList.add(i);
		}*/
		//inputParam = inputParam * 0.1234;
		//inputParam = null;
		
		System.out.println("INPUT_PARAM OP1: " + inputParam);
		return inputParam;
	}

}
