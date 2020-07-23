package standrews.cs5099.mpp.tasks;

import java.util.concurrent.ExecutorService;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;

/**
 * SIMPLE OPERATION
 * 
 * @author Gursher
 *
 */
public class SimpleWorker extends Worker {

	// Actual data to be executed by the worker
	private Object data;

	// Instruction to be executed by the Worker
	private Instruction instruction;

	// Queue<Object> outputQueue;

	/**
	 * Constructor for Root Worker
	 * 
	 * @param data
	 * @param assignedTaskExecutor
	 */
	public SimpleWorker(ExecutorService taskExecutor, Instruction instruction) {

		// this.rootWorker = this;
		this.taskExecutor = (TaskExecutor) taskExecutor;
		this.instruction = instruction;
		this.taskFuture = new TaskFuture(this.taskExecutor, this);
		this.priority = 10000;
		this.isFinished = false;

	}

	@Override
	public void run() {
		this.data = inputQueue.remove();
		// this.data = inputQueue.remove();
		// TODO Auto-generated method stub
		//outputQueue.add(WorkerService.executeInstruction(data, instruction));
		this.taskFuture.setResult(WorkerService.executeInstruction(data, instruction));
	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Instruction getInstruction() {
		// TODO Auto-generated method stub
		return null;
	}

}
