package standrews.cs5099.mpp.tests;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class Operation1a implements Operation<Integer, Double> {
	@Override
	public Double execute(Integer inputParam) throws Exception {
		/*
		 * for(int i=1; i<=1000000; i++) { for(int j = 1; j<=1000000; j++) {
		 * 
		 * } }
		 */	
		Double outputParam = (double)inputParam * 10;
		return outputParam;
	}

}
