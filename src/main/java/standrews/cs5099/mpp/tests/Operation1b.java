package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation1b implements Operation<List<Integer>, List<Integer>> {
	@Override
	public List<Integer> execute(List<Integer> inputParam) throws Exception {
		List<Integer> tempList = new ArrayList<>();
		// usage of streams are possible
		// inputParam.stream().forEach(action);
		for (int i : inputParam) {
			i = i/5;
			tempList.add(i);
		}
		inputParam = null;
		return tempList;
	}
}
