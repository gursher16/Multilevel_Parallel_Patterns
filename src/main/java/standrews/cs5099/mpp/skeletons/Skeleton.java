package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.instructions.InstructionsBuilder;

/**
 * 
 * @author Gursher
 *
 * @param <I>
 * @param <O>
 */
public interface Skeleton<I, O> {

	/**
	 * Method to execute a given operation
	 * 
	 * @param inputOperation The task to be executed in parallel
	 * @return A <code>Future</code> object representing asynchronous execution of
	 *         the input task
	 */
	public Future<O> submitData(I inputData);

	/**
	 * Method to execute a set of given tasks
	 * 
	 * @param inputTasks
	 * @return
	 */
	// public Future<O>[] executeTask(I[] inputTasks);
	
	public void buildInstructions(InstructionsBuilder instructionsBuilder);
}
