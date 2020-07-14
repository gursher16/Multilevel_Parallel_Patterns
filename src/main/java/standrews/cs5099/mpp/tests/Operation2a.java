package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation2a implements Operation<Integer, Integer>{
	@Override
	public Integer execute(Integer inputParam) throws Exception {
		/**
		List<Integer> tempList = new ArrayList<>();
		for (Integer i : inputParam) {
			i = i * 11;
			tempList.add(i);
		}
		inputParam = null;**/
		inputParam = inputParam * 11;
		return inputParam;
	}
}
