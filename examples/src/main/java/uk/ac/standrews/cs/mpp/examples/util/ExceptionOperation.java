package uk.ac.standrews.cs.mpp.examples.util;

import java.util.ArrayList;
import java.util.List;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class ExceptionOperation implements Operation<List<Integer>, List<Double>> {

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
			throw new Exception("AAAAAAAAAAA");
			
		}
		return result;
	}

}
