package standrews.cs5099.mpp.tasks;

import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.text.AbstractWriter;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;

public class PipelineWorker<O> extends Worker {

	// Actual data to be executed by the worker
	private Object data;

	// Parent Worker (pipeline stage)
	// private Worker parentWorker;
	
	// private Worker childWorker;

	// Execution to be executed by the Worker
	private Instruction instruction;

	// out

	/**
	 * Constructor for Root Worker
	 * 
	 * @param data
	 * @param assignedTaskExecutor
	 */
	public PipelineWorker(ExecutorService taskExecutor, Instruction instruction) {

		// this.rootWorker = this;
		this.taskExecutor = (TaskExecutor) taskExecutor;
		this.instruction = instruction;
		this.taskFuture = new TaskFuture(this.taskExecutor, this);
		this.childWorker = null;
		this.priority = 10000;
		this.isFinished = false;

		inputQueue = new LinkedBlockingQueue<Object>();
		outputQueue = new LinkedBlockingQueue<Object>();
	}

	/**
	 * 
	 */
	public PipelineWorker(Object data, ExecutorService assignedTaskExecutor, Instruction instruction) {
		this.data = data;
		// this.rootWorker= parent;
		this.taskExecutor = (TaskExecutor) assignedTaskExecutor;
		this.instruction = instruction;
		this.taskFuture = new TaskFuture(this.taskExecutor, this);
	}

	/**
	 * Returns true if current task is a Root task (a Task with no parent)
	 * 
	 * @return
	 */
	public boolean isRootPipelineWorker() {
		return (getParentPipelineWorker() == null);
	}

	/**
	 * Method which is invoked by an thread issued by the executor service
	 * 
	 */

	@Override
	public void run() {

		if (this.isRootPipelineWorker()) {
			boolean isChildRunning = false;

			while (!this.inputQueue.isEmpty()) {
				this.data = inputQueue.remove();
				System.out.println("STAGE 1 - computing..");

				Object result = WorkerService.executePipelineWorker(this);
				this.outputQueue.offer(result);
				if (!isChildRunning) {
					// invoke next worker
					taskExecutor.execute(this.childWorker);
					isChildRunning = true;
				}
			}
			// push value to output queue to signal end of execution
			System.out.println("ENDING- STAGE 1 !!!!");
			this.outputQueue.add(new String("END"));

		} else {
			// System.out.println("Child Worker is being executed");
			try {
				if (null != this.getChildPipelineWorker()) {
					/** CURRENT WORKER IS INTERMEDIATE WORKER IN PIPELINE **/
					synchronized (this) {
						boolean isChildRunning = false;

						while (this.getParentWorker().outputQueue.peek() != "END") {

							if (null == this.getParentWorker().outputQueue.peek()) {
								/** Keep waiting till non null value in inputqueue **/
								System.out.println("STAGE 2 - waiting..");
								continue;
							}
							if (this.getParentWorker().outputQueue.peek().equals("END")) {
								/** If END in inputQueue then put END in outputQueue and terminate **/
								break;
							}
							System.out.println("STAGE 2 - computing..");
							this.data = this.getParentWorker().outputQueue.remove();

							this.outputQueue.offer(WorkerService.executePipelineWorker(this));
							if (!isChildRunning) {
								taskExecutor.execute(this.childWorker);
								isChildRunning = true;
							}
						}
						this.outputQueue.add(new String("END"));
						System.out.println("ENDING - STAGE 2 !!!!");
					}

				} else { /* CURRENT WORKER IS LAST WORKER IN PIPELINE */
					// call special method which executes and stores result in a Collection
					// WorkerService.executeAndCollectResult(this);
					synchronized (this) {

						while (this.getParentWorker().outputQueue.peek() != "END") {

							if (null == this.getParentWorker().outputQueue.peek()) {
								/** Keep waiting till non null value in inputqueue **/
								System.out.println("STAGE 3 - waiting..");
								continue;
							}
							if (this.getParentWorker().outputQueue.peek().equals("END")) {
								/** If END in inputQueue then put END in outputQueue and terminate **/
								break;
							}
							System.out.println("STAGE 3 - computing..");
							this.data = this.getParentWorker().outputQueue.remove();

							this.outputQueue.add(WorkerService.executePipelineWorker(this));

						}
						System.out.println("ENDING PIPELINE !!!!!");
						this.taskFuture.setResult("FINISH");
					}
					// set future of
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// data = this.getParentWorker().getFuture()
		}

		this.isFinished = true;
	}

	public Worker getchildWorker() {
		return this.childWorker;
	}

	public boolean isTaskFinished() {
		/** dummy return value **/
		return true;
	}

	public TaskFuture getFuture() {

		return this.taskFuture;
	}

	public Worker getParentWorker() {
		return this.parentWorker;
	}

	public PipelineWorker getParentPipelineWorker() {

		if (this.parentWorker instanceof PipelineWorker) {
			return (PipelineWorker) parentWorker;
		} else {
			return null;
		}
	}

	public PipelineWorker getChildPipelineWorker() {
		if (this.childWorker instanceof PipelineWorker) {
			return (PipelineWorker) childWorker;
		} else {
			return null;
		}
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

	public RootWorker getRootWorker() {
		return rootWorker;
	}

	public void setRootWorker(RootWorker rootWorker) {
		this.rootWorker = rootWorker;
	}

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setAssignedTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setParentPipelineWorker(PipelineWorker parentWorker) {
		this.parentWorker = parentWorker;
	}

	public synchronized boolean areChildTasksFinished() {
		// dummy value
		return true;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}

}
