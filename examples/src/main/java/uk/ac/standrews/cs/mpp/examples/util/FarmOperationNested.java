package uk.ac.standrews.cs.mpp.examples.util;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class FarmOperationNested implements Operation<Integer, Integer> {

	@Override
	public Integer execute(Integer inputParam) throws Exception {
		fib(inputParam);
		return inputParam;
	}
	
	private static int fib(int n) {
		if(n<=1) {
			return n;
		}
		return (fib(n-1) + fib(n-2));
	}


}
