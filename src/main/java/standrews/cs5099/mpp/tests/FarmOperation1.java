package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class FarmOperation1 implements Operation<List<Integer>, List<Double>> {

	@Override
	public List<Double> execute(List<Integer> inputParam) throws Exception {
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
		
	//	System.out.println("INPUT_PARAM OP1: " + inputParam);
		List<Double> result = new ArrayList<>();
		for(int i : inputParam) {
			double o = 1/i;
			o = o*0.5;
			o = o/1.23;
			result.add(o);
		}
		return result;
	}

}
