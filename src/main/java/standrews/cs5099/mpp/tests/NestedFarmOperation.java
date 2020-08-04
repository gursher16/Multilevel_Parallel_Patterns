package standrews.cs5099.mpp.tests;

import standrews.cs5099.mpp.operations.Operation;

public class NestedFarmOperation implements Operation<Integer, Integer> {

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
