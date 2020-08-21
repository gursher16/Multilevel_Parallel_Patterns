package uk.ac.standrews.cs.mpp.examples.util;

import java.util.ArrayList;
import java.util.List;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class FarmOperation1 implements Operation<List<Integer>, List<Double>> {

	@Override
	public List<Double>execute(List<Integer> inputParam) throws Exception {
		
		for(int i: inputParam) {
			fib(i);	
		}		
		return new ArrayList<Double>();
	}
	
	private static int fib(int n) {
		if(n<=1) {
			return n;
		}
		return (fib(n-1) + fib(n-2));
	}

}
