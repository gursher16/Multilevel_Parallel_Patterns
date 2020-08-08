package standrews.cs5099.mpp.workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.skeletons.Skeleton;
import standrews.cs5099.mpp.util.Constants;

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

		// Collect results and add each result to outputQueue of FarmWorker
		futureList.stream().forEach((future) -> {
			try {
				// Block for result
				Object result = future.get();
				// this.outputQueue.addAll((Collection<? extends Object>) result);
				//this.outputQueue.add(Constants.END);
			} catch (InterruptedException | ExecutionException e1) {

				e1.printStackTrace();
			}
		});

		// Send END signal to child worker to signal no more tasks
		if (null != this.getChildWorker()) {
			this.outputQueue.add(Constants.END);
		}
		this.taskFuture.setResult(this.outputQueue);
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
