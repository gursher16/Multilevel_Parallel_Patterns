package uk.ac.standrews.cs.mpp.operations;

/**
 * An Operation containing the actual business logic -- to be implemented by the
 * user
 * 
 * @author Gursher
 *
 * @param <I> Input Parameter Type
 * @param <O> Output Parameter Type
 */
public interface Operation<I, O> {

	/**
	 * Compute and return the result
	 * 
	 * @param InputParam
	 * @return
	 * @throws Exception
	 */
	public O execute(I inputParam) throws Exception;
	
	
	// worker here
}
