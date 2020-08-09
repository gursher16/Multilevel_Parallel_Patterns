package standrews.cs5099.mpp.workers;

import java.util.concurrent.ExecutorService;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.util.Constants;

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
	 * Constructs a SimpleWorker
	 * 
	 * @param taskExecutor
	 * @param instruction
	 * @param farmworker
	 */
	public SimpleWorker(ExecutorService taskExecutor, Instruction instruction, FarmWorker farmWorker) {

		this.taskExecutor = (TaskExecutor) taskExecutor;
		this.instruction = instruction;
		this.taskFuture = new TaskFuture(this.taskExecutor, this);
		this.priority = 10000;
		this.isFinished = false;
		this.farmWorker = farmWorker;

	}

	@Override
	public void run() {

		while (this.inputQueue.peek() != Constants.END) {
			if (null == this.inputQueue.peek()) {
				/** Keep waiting till non null value in input queue **/
				// System.out.println("Worker - waiting..");
				// waitCount+=1;
				continue;
			}
			if (this.inputQueue.peek().equals(Constants.END)) {
				/** Break if END signal is received **/
				break;
			}

			this.data = this.inputQueue.remove();
			// System.out.println("Data :" + data);
			// System.out.println("Worker - computing..");
			// execute instruction and store result in future
			if (null != this.farmWorker) {
				this.farmWorker.outputQueue.offer(WorkerService.executeWorker(this));
			} else {
				this.outputQueue.offer(WorkerService.executeWorker(this));
			}

		}

		this.taskFuture.setResult(this.outputQueue);
		// System.out.println("Shutting down worker..");

	}

	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return this.data;
	}

	@Override
	public Instruction getInstruction() {
		// TODO Auto-generated method stub
		return this.instruction;
	}

}
