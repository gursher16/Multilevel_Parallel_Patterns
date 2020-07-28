package standrews.cs5099.mpp.tasks;

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

		while (this.inputQueue.peek() != Constants.END) {
			if (null == this.inputQueue.peek()) {
				/** Keep waiting till non null value in input queue **/
				// System.out.println("Worker - waiting..");
				// waitCount+=1;
				continue;
			}
			if(this.inputQueue.peek().equals(Constants.END)) {
				/** Break if END signal is received **/
				break;
			}
			
			this.data = this.inputQueue.remove();
		//System.out.println("Data :" + data);
			// System.out.println("Worker - computing..");
			// execute instruction and store result in future
			this.outputQueue.offer(WorkerService.executeInstruction(data, instruction));

		}

		//System.out.println("Shutting down worker..");
		this.taskFuture.setResult(this.outputQueue);
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
