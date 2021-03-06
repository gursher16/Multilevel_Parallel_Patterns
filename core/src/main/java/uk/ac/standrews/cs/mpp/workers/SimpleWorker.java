package uk.ac.standrews.cs.mpp.workers;

import java.util.concurrent.ExecutorService;

import uk.ac.standrews.cs.mpp.core.TaskExecutor;
import uk.ac.standrews.cs.mpp.core.WorkerService;
import uk.ac.standrews.cs.mpp.instructions.Instruction;
import uk.ac.standrews.cs.mpp.util.Constants;

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
		isExceptionThrown = false;

	}

	@Override
	public void run() {

		while (this.inputQueue.peek() != Constants.END) {
			Object result = null;
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
			try {
				result = WorkerService.executeWorker(this);
			}
			catch(Exception ex) {
				isExceptionThrown = true;
				this.outputQueue.clear();
				this.outputQueue.add(ex);
				this.taskFuture.setResult(ex);
				break;
			}
			if (null != this.farmWorker) {				
				this.farmWorker.outputQueue.offer(result);
			} else {
				this.outputQueue.offer(result);
			}

		}
		if(!isExceptionThrown) {
			this.taskFuture.setResult(this.outputQueue);
		}
		
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
