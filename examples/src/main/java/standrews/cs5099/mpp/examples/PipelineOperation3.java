package standrews.cs5099.mpp.examples;

import java.util.ArrayList;
import java.util.List;

import standrews.cs5099.mpp.operations.Operation;

public class PipelineOperation3 implements Operation<Integer, Integer> {
	@Override
	public Integer execute(Integer inputParam) throws Exception {
		/*
		 * for(int i=1; i<=1000000; i++) { for(int j = 1; j<=1000000; j++) {
		 * 
		 * } }
		 */	
		//Double outputParam = (double)inputParam * 0.5;
		//return outputParam;
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
