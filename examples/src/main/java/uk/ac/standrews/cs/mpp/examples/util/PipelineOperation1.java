package uk.ac.standrews.cs.mpp.examples.util;

import java.util.ArrayList;
import java.util.List;

import uk.ac.standrews.cs.mpp.operations.Operation;

public class PipelineOperation1 implements Operation<Integer, Integer> {

	@Override
	public Integer execute(Integer inputParam) throws Exception {
		
		/*
		 * for(int i=1; i<=1000000; i++) { for(int j = 1; j<=1000000; j++) {
		 * 
		 * } }
		 */
		//return inputParam * 10;
		
		//fibonacci
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
