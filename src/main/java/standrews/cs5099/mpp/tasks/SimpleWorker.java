package standrews.cs5099.mpp.tasks;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

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

	// ExecutorService responsible for executing worker
	private TaskExecutor taskExecutor;
	// Future object which holds result of computation
	private TaskFuture taskFuture;
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
		// TODO Auto-generated method stub
		WorkerService.executeInstruction(data, instruction);

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
