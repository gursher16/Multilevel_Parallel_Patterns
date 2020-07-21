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
		WorkerService.initializeTaskPool(data);
		if (data instanceof Collection) {
			
				iter+=1;
				for(Object o : (Collection)data) {
					workers[0].inputQueue.add(o);
				}
				//workers[0].setData(o);
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
				//this.taskExecutor.execute(workers[0]);
				
				/***1st approach*/
				//while(WorkerService.index != WorkerService.taskPool.length-1) {
					//try {
						// block and get result of last worker (which is effectively the result of the
						// skeleton)
						/**block till 1st worker is free**/
						this.taskExecutor.execute(workers[0]);
						//Object result = workers[0].getFuture().get();
						// set result of last worker's future object to null -- preparing it for next iteration
						//workers[workers.length - 1].getFuture().setResult(null);
						//System.out.println("Iteration: " + iter + "  Result: " + result);
						// send result for collection
						//collectResult(result);
					//} catch (InterruptedException e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					//} catch (ExecutionException e) {
						// TODO Auto-generated catch block
					//	e.printStackTrace();
					//} 				
			//	}
				/** BLOCK FOR RESULT**/
				//workers
				//WorkerService.resultFuture.get();
				try {
					workers[workers.length - 1].getFuture().get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				skelFuture.setResult(WorkerService.fetchResult(workers[workers.length-1].outputQueue));
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
