package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation2 implements Operation<Integer, Integer> {

	@Override
	public Integer execute(Integer inputParam) throws Exception {
		/*
		 * for(int i=1; i<=1000000; i++) { for(int j = 1; j<=1000000; j++) {
		 * 
		 * } }
		 */
	
		return inputParam + 111;
	}

}
