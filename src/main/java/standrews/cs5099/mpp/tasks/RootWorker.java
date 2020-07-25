package standrews.cs5099.mpp.tasks;

import java.util.Collection;
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
	// Future object which holds result of skeleton
	private TaskFuture skelFuture;
	private Class<O> outputType;
	private Collection result;

	private int priority;

	// final result
	// private Collection result;

	private Worker[] workers;

	/**
	 * Constructor for Root Task
	 * 
	 * @param data
	 * @param assignedTaskExecutor
	 */
	public RootWorker(Object data, ExecutorService taskExecutor, Worker[] workers, Class<O> outputType) {
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
		WorkerService.initializeTaskPool(data);
		if (data instanceof Collection) {

			iter += 1;
			for (Object o : (Collection) data) {
				workers[0].inputQueue.add(o);
			}
			// add END to tail of queue to signal end of computation to all workers
			workers[0].inputQueue.add("END");
			this.taskExecutor.execute(workers[0]);

			try {
				workers[workers.length - 1].getFuture().get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			skelFuture.setResult(WorkerService.fetchResult(workers[workers.length - 1].outputQueue));
		}

		this.isFinished = true;
	}

	private void collectResult(Object result) {
		this.result.add(result);
	}

	public Future getSkelFuture() {
		return this.skelFuture;
	}

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
