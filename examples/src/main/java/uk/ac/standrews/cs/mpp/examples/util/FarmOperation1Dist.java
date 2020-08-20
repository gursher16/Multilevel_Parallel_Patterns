package uk.ac.standrews.cs.mpp.examples.util;

import java.util.ArrayList;
import java.util.List;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class FarmOperation1Dist implements Operation<List<Object>, List<Object>> {

	@Override
	public List<Object> execute(List<Object> inputParam) throws Exception {
		
	//	System.out.println("INPUT_PARAM OP1: " + inputParam);
		List<Object> result = new ArrayList<>();
		for(Object i: inputParam) {
			Integer li = (Integer)i;
			double o = 1/li;
			o = o*0.5;
			o = o/1.23;
			result.add(o);
		}
		return result;
	}

}
