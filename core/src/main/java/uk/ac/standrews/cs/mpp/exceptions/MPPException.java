package uk.ac.standrews.cs.mpp.exceptions;

import uk.ac.standrews.cs.mpp.util.Constants;

public class MPPException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MPPException(Throwable e) {
		super(Constants.OPERATION_ERROR_MSG, e);		
	}
	
	public void printExceptionDetails() {
		System.err.println("ERROR CODE: " + Constants.OPERATION_ERROR_CODE);
		System.err.println("ERROR MESSAGE: " + Constants.OPERATION_ERROR_MSG);
	}

}
