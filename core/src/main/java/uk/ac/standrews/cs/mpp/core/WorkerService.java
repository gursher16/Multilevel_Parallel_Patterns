package uk.ac.standrews.cs.mpp.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import uk.ac.standrews.cs.mpp.instructions.Instruction;
import uk.ac.standrews.cs.mpp.workers.PipelineWorker;
import uk.ac.standrews.cs.mpp.workers.TaskFuture;
import uk.ac.standrews.cs.mpp.workers.Worker;

/**
 * Utility class used by instances of {@link Worker} to perform miscellaneous
 * tasks
 * 
 * @author Gursher
 *
 */
public class WorkerService {

	// Holds the result of the skeleton execution
	public static Collection result;

	// Future object for the result of the skeleton execution
	public static TaskFuture resultFuture;

	/**
	 * Creates an {@link Collection} instance for storing result of the skeleton
	 * execution based on the given <code>outputType</code> parameter
	 * 
	 * @param outputType The type of output - has to be an implementation of
	 *                   {@link Collection}
	 */
	public static void createInstanceOfResult(Class<?> outputType) {
		if (null == outputType) {
			result = null;
		} else if (outputType.equals(ArrayList.class)) {
			result = new ArrayList<Object>();
		} else if (outputType.equals(LinkedList.class)) {
			result = new LinkedList<Object>();
		} else if (outputType.equals(Vector.class)) {
			result = new Vector<Object>();
		} else if (outputType.equals(Stack.class)) {
			result = new Stack<Object>();
		} else if (outputType.equals(PriorityQueue.class)) {
			result = new PriorityQueue<Object>();
		} else if (outputType.equals(ArrayDeque.class)) {
			result = new ArrayDeque<>();
		} else if (outputType.equals(HashSet.class)) {
			result = new HashSet<Object>();
		} else if (outputType.equals(LinkedHashSet.class)) {
			result = new LinkedHashSet<Object>();
		} else if (outputType.equals(TreeSet.class)) {
			result = new TreeSet<Object>();
		}
	}

	/**
	 * Executes the {@link Instruction} held by a {@link Worker}
	 * 
	 * @param worker The <code>Worker</code> holding an <code>Instruction</code>
	 * @return Returns result of executing an <code>Instruction</code>
	 * @throws Exception Any exception during execution of the user
	 *                   defined<code>Operation</code>
	 */
	public static Object executeWorker(Worker worker) throws Exception {
		Object data = worker.getData();
		data = worker.getInstruction().executeInstruction(data, null, null);
		return data;
	}

	/**
	 * Collects elements from the <code>outputQueue</code> and adds it to the
	 * <code>result</code> collection
	 * 
	 * @param outputQueue The <code>outputQueue</code> of the last {@link Worker} in
	 *                    an array of Workers
	 * @return A <code>Collection</code> of type specified by the user
	 */
	public static Collection fetchResult(Queue<Object> outputQueue) {
		// only collect if result is not null
		if (result != null) {
			for (Object o : outputQueue) {
				result.add(o);
			}
		} else {
			// create empty List for result
			result = new ArrayList<>();
		}

		// resultFuture.setResult(result);
		return result;
	}

	public static void setResultFuture(TaskFuture resultFuture) {
		WorkerService.resultFuture = resultFuture;
	}

	/** dummy **/
	private static synchronized void collectResult(Object data) {
		result.add(data);
	}

	public static void notifyParent(PipelineWorker task) {

	}

}
