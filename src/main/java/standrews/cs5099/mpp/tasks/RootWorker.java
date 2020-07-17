package standrews.cs5099.mpp.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.TaskExecutor;

/**
 * Wrapper for all workers
 * 
 * @author Gursher
 *
 */
public class RootWorker<O> extends Worker {
	// Actual data to be executed
	private Object data;
	// ExecutorService responsible for executing tasks
	private TaskExecutor taskExecutor;
	// Future object which holds result of skeleton
	private TaskFuture skelFuture;
	private Class<O> outputType;
	private Collection result;
	
	private int priority;

	// final result
	// private Collection result;

	private PipelineWorker[] workers;

	/**
	 * Constructor for Root Task
	 * 
	 * @param data
	 * @param assignedTaskExecutor
	 */
	public RootWorker(Object data, ExecutorService taskExecutor, PipelineWorker[] workers, Class<O> outputType) {
		this.data = data;
		// this.rootTask = this;
		this.taskExecutor = (TaskExecutor) taskExecutor;
		this.workers = workers;
		this.outputType = outputType;
		this.priority = Integer.MAX_VALUE;
		// this.instructionStack = instructionStack;
		skelFuture = new TaskFuture(this.taskExecutor, this);
	}

	/**
	 * Method which is invoked by an thread issued by the executor service
	 * 
	 */
	@Override
	public void run() {
		System.out.println("MAIN WORKER INITIATED");
		createInstanceOfResult();
		executeSkeleton();
		// WorkerService.executeTask(this);
		/**
		 * O result = (O) WorkerService.executeTask(this); return null;
		 **/
	}

	public boolean isTaskFinished() {
		/** dummy return value **/
		return true;
	}

	public TaskFuture getFuture() {

		// return rootTask.taskFuture;
		return null;
	}

	/**
	 * Fetch data field of a Task
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Update the data field of the Task
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}

	///////////////////

	// public Future<O> h

	public void executeSkeleton() {
		int iter = 0;
		if (data instanceof Collection) {
			for (Object o : (Collection) data) {
				iter+=1;
				workers[0].setData(o);
				// start top level worker in skeleton (which will invoke subsequent workers)
				this.taskExecutor.execute(workers[0]);
				try {
					// block and get result of last worker (which is effectively the result of the
					// skeleton)
					Object result = workers[workers.length - 1].getFuture().get();
					// set result of last worker's future object to null -- preparing it for next iteration
					workers[workers.length - 1].getFuture().setResult(null);
					System.out.println("Iteration: " + iter + "  Result: " + result);
					// send result for collection
					collectResult(result);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			skelFuture.setResult(result);
		}
		this.isFinished = true;
	}

	private void collectResult(Object result) {
		this.result.add(result);
	}

	private void createInstanceOfResult() {
		
		if (outputType.equals(ArrayList.class)) {
			result = new ArrayList<Object>();
		} /*else if (resultType instanceof LinkedList) {
			result = new LinkedList<Object>();
		} else if (resultType instanceof Vector) {
			result = new Vector<Object>();
		} else if (resultType instanceof Stack) {
			result = new Stack<Object>();
		} else if (resultType instanceof PriorityQueue) {
			result = new PriorityQueue<Object>();
		} else if (resultType instanceof ArrayDeque) {
			result = new ArrayDeque<>();
		} else if (resultType instanceof HashSet) {
			result = new HashSet<Object>();
		} else if (resultType instanceof LinkedHashSet) {
			result = new LinkedHashSet<Object>();
		} else if (resultType instanceof TreeSet) {
			result = new TreeSet<Object>();
		}*/

	}
	
	public Future getSkelFuture() {
		return this.skelFuture;
	}

	///////////////////

	/*
	public void setRootTask(MPPTask rootTask) {
		this.rootTask = rootTask;
	}

	public List<MPPTask> getChildTasks() {
		return childTasks;
	}

	public void setChildTasks(List<MPPTask> childTasks) {
		this.childTasks = childTasks;
	}

	public TaskExecutor getAssignedTaskExecutor() {
		return assignedTaskExecutor;
	}

	public void setAssignedTaskExecutor(TaskExecutor assignedTaskExecutor) {
		this.assignedTaskExecutor = assignedTaskExecutor;
	}

	public void setParentTask(MPPTask parentTask) {
		this.parentTask = parentTask;
	}
*/
	public synchronized boolean areChildTasksFinished() {
		// dummy value
		return true;
	}
	
	
	
}
