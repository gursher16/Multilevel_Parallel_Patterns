package standrews.cs5099.mpp.tasks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;

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
		WorkerService.createInstanceOfResult(outputType);
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
				
				///2nd approach////
				
				
				/*
				if(taskExecutor.getActiveCount()<taskExecutor.getCorePoolSize()) {
					this.taskExecutor.execute(workers[0]);					
				}
				else {
					try {
						synchronized (this) {
							wait();
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.taskExecutor.execute(workers[0]);
				}
				*/
				this.taskExecutor.execute(workers[0]);
				
				/**
				 * 	1st approach
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
				
				
				
				****/
			}
			
			skelFuture.setResult(WorkerService.fetchResult());
		}
		this.isFinished = true;
	}

	private void collectResult(Object result) {
		this.result.add(result);
	}

	private void createInstanceOfResult() {
		
		if (outputType.equals(ArrayList.class)) {
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

	@Override
	public Instruction getInstruction() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
