package uk.ac.standrews.cs.mpp.workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import uk.ac.standrews.cs.mpp.core.TaskExecutor;
import uk.ac.standrews.cs.mpp.instructions.Instruction;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;
import uk.ac.standrews.cs.mpp.util.Constants;

public class FarmWorker extends Worker {

	// Actual data to be executed by the worker
	private Object data;

	// Instruction to be executed by the Worker
	private Instruction instruction;

	// stack of instructions to be replicated by farm
	private Stack<Instruction> instructionStack;

	// skeleton to be replicated by farm
	private Skeleton<?, ?> targetSkeleton;
	// Queue<Object> outputQueue;

	private List<TaskFuture> futureList;

	private int noOfWorkers;

	public FarmWorker(ExecutorService taskExecutor, Skeleton targetSkeleton, int noOfWorkers,
			Stack<Instruction> instructionStack) {

		// this.rootWorker = this;
		this.taskExecutor = (TaskExecutor) taskExecutor;
		// this.instruction = instruction;
		this.taskFuture = new TaskFuture(this.taskExecutor, this);
		/** latest fields **/
		this.targetSkeleton = targetSkeleton;
		this.instructionStack = instructionStack;
		this.futureList = new ArrayList<>();
		/****/
		this.priority = 10000;
		this.isFinished = false;
		this.noOfWorkers = noOfWorkers;
		isExceptionThrown = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int index = 0;
		List<Worker[]> farmWorkers = new ArrayList<>();
		for (int i = 0; i < noOfWorkers; i++) {
			// create array of workers (will contain at least two workers if target skeleton
			// is a pipeline)
			Stack<Instruction> copyStack = (Stack<Instruction>) instructionStack.clone();
			Worker[] workers = WorkerBuilder.createWorkers(targetSkeleton, taskExecutor, copyStack, this);
			farmWorkers.add(workers);
			// execute the worker array
			taskExecutor.execute(workers[0]);
			// add future of last worker in worker array to a list of futures
			futureList.add(workers[workers.length - 1].getFuture());
		}

		// Invoke child worker if any
		if (null != this.getChildWorker()) {
			taskExecutor.execute(childWorker);
		}

		while (this.inputQueue.peek() != Constants.END) {
			if (null == this.inputQueue.peek()) {
				// System.out.println("Waiting....");
				/** Keep waiting till non null value in input queue **/
				continue;
			} else if (this.inputQueue.peek() == Constants.END) {
				System.out.println("Handled Race CONDITION!!!");
				break;
			}
			if (index < farmWorkers.size()) {
				this.data = inputQueue.remove();
				// System.out.println(data);
				// System.out.println("Removing....");
				Worker[] worker = farmWorkers.get(index);
				worker[0].inputQueue.offer(data);
				/*** Try and get result of farmworker and insert result in outputQueue **/

				/***/
				index += 1;
				if (index == farmWorkers.size()) {
					index = 0;
				}
			}
		}

		// Send END signal to all workers to signal no more tasks
		farmWorkers.stream().forEach((workers) -> workers[0].inputQueue.add("END"));
		
		// Block for results and check for Exception
		for(TaskFuture future: futureList) {
			try {
				Object result = future.get();
				if(result instanceof Exception) {					
					isExceptionThrown = true;
					this.outputQueue.clear();
					this.outputQueue.add(result);
					this.taskFuture.setResult(result);
					// attempt to shut down executor service
					taskExecutor.shutdown();
					break;
				}
			} catch (InterruptedException | ExecutionException e) {
				isExceptionThrown = true;
				this.outputQueue.clear();
				this.outputQueue.add(e);
				this.taskFuture.setResult(e);
				// attempt to shut down executor service
				taskExecutor.shutdown();
			}
		}	

		// Send END signal to child worker to signal no more tasks
		if (null != this.getChildWorker()) {
			this.outputQueue.add(Constants.END);
		}
		if(!isExceptionThrown) {
			this.taskFuture.setResult(this.outputQueue);
		}		
	}

	@Override
	public Instruction getInstruction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
