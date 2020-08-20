package uk.ac.standrews.cs.mpp.workers;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

import uk.ac.standrews.cs.mpp.core.TaskExecutor;
import uk.ac.standrews.cs.mpp.core.WorkerService;
import uk.ac.standrews.cs.mpp.instructions.Instruction;
import uk.ac.standrews.cs.mpp.instructions.SeqOpInstruction;
import uk.ac.standrews.cs.mpp.util.Constants;

public class PipelineWorker<O> extends Worker {

	// Actual data to be executed by the worker
	private Object data;

	// Parent Worker (pipeline stage)
	// private Worker parentWorker;

	// private Worker childWorker;

	// Execution to be executed by the Worker
	private Instruction instruction;

	/** variables for testing thread waiting **/

	long waitCount;	
	

	/****/

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

		waitCount = 0;
		isExceptionThrown = false;
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
		return (getParentWorker() == null);
	}

	/**
	 * Method which is invoked by an thread issued by the executor service
	 * 
	 */

	@Override
	public void run() {

		if (this.isRootPipelineWorker()) {
			synchronized (this) {
				boolean isChildRunning = false;
				Object result = null;
				while (this.inputQueue.peek() != Constants.END) {
					
					if (!isChildRunning) {
						// invoke next worker
						taskExecutor.execute(this.childWorker);
						isChildRunning = true;
					}
					
					if (null == this.inputQueue.peek()) {
						/** Keep waiting till non null value in input queue **/
						// System.out.println("STAGE 1 - waiting..");
						waitCount += 1;
						continue;
					}
					if (this.inputQueue.peek().equals(Constants.END)) {
						break;
					}
					this.data = inputQueue.remove();
					// System.out.println("STAGE 1 - computing..");
					try {
						result = WorkerService.executeWorker(this);
					}
					catch(Exception ex) {
						isExceptionThrown = true;
						this.outputQueue.clear();
						this.outputQueue.add(ex);
						// attempt to shut down executor service
						taskExecutor.shutdown();
						break;
					}
					if (null != result) {

						if (((SeqOpInstruction) this.instruction).doSplitOutput) {
							if (result instanceof Collection) {
								for (Object o : (Collection) result) {
									this.outputQueue.offer(o);
								}
							}
						} else {
							this.outputQueue.offer(result);
						}
					}
					
				}
				// push END to output queue to signal end of execution
				System.out.println("**************ENDING- STAGE 1 !!!!");
				System.out.println("**************STAGE 1 waiting = " + waitCount);
				this.outputQueue.add(Constants.END);
			}
		} else {
			// System.out.println("Child Worker is being executed");		
				if (null != this.getChildWorker()) {
					/** CURRENT WORKER IS INTERMEDIATE WORKER IN PIPELINE **/
					synchronized (this) {
						boolean isChildRunning = false;
						Object result = null;
						while (this.inputQueue.peek() != Constants.END) {
							
							if (!isChildRunning) {
								taskExecutor.execute(this.childWorker);
								isChildRunning = true;
							}

							if (null == this.inputQueue.peek()) {

								/** Keep waiting till non null value in input queue **/
								// System.out.println("STAGE 2 - waiting..");
								waitCount += 1;
								continue;
							}
							if (this.inputQueue.peek().equals(Constants.END)) {
								/** If END in inputQueue then terminate **/
								break;
							}
							if(this.inputQueue.peek() instanceof Exception) {
								/** If Exception in inputQueue then put exception in outputQueue and terminate **/
								isExceptionThrown = true;
								this.outputQueue.add(this.inputQueue.remove());								
								break;
							}
							// System.out.println("STAGE 2 - computing..");
							this.data = this.inputQueue.remove();
							try {
								result = WorkerService.executeWorker(this);
							}
							catch(Exception ex) {
								isExceptionThrown = true;
								this.outputQueue.clear();
								this.outputQueue.add(ex);						
								// attempt to shut down executor service
								taskExecutor.shutdown();
								break;
							}
							if (null != result) {
								if (((SeqOpInstruction) this.instruction).doSplitOutput) {
									if (result instanceof Collection) {
										for (Object o : (Collection) result) {
											this.outputQueue.offer(o);
										}
									}
								} else {
									this.outputQueue.offer(result);
								}
							}
							
						}
						System.out.println("***********ENDING - STAGE 2 !!!!");
						System.out.println("***********STAGE 2 waiting = " + waitCount);
						this.outputQueue.add(Constants.END);

					}

				} else { /* CURRENT WORKER IS LAST WORKER IN PIPELINE */

					synchronized (this) {
						Object result = null;
						while (this.inputQueue.peek() != Constants.END) {

							if (null == this.inputQueue.peek()) {
								/** Keep waiting till non null value in inputqueue **/
								// System.out.println("STAGE 3 - waiting..");
								waitCount += 1;
								continue;
							}
							if (this.inputQueue.peek().equals(Constants.END)) {
								/** If END in inputQueue then put END in outputQueue and terminate **/
								break;
							}
							if(this.inputQueue.peek() instanceof Exception) {
								isExceptionThrown = true;
								this.taskFuture.setResult(this.inputQueue.remove());
								break;
							}
							// System.out.println("STAGE 3 - computing..");
							this.data = this.inputQueue.remove();
							
							try {
								result = WorkerService.executeWorker(this);
							}
							catch(Exception ex) {
								isExceptionThrown = true;
								this.outputQueue.clear();
								this.taskFuture.setResult(ex);
								// attempt to shut down executor service
								taskExecutor.shutdown();
								break;
							}
							if (null != this.farmWorker) {// Compute and add result to output queue of FarmWorker if
															// this pipeline is farmed
								if (null != result) { // Special cases when result of computation is null
									this.farmWorker.outputQueue.add(result);
								}

							} else {// Compute and add result to output queue if this pipeline is not farmed

								if (null != result) { // Special cases when result of computation is null
									this.outputQueue.add(result);
								}
							}
						}
					System.out.println("**************ENDING PIPELINE !!!!!");
					System.out.println("**************STAGE 3 waiting = " + waitCount);
					
					if (!isExceptionThrown) {
						this.taskFuture.setResult(this.outputQueue);
					}
				}

			}

		}

		this.isFinished = true;
	}

	public boolean isTaskFinished() {
		/** dummy return value **/
		return true;
	}

	public TaskFuture getFuture() {

		return this.taskFuture;
	}

	/*
	 * public PipelineWorker getParentPipelineWorker() {
	 * 
	 * if (this.parentWorker instanceof PipelineWorker) { return (PipelineWorker)
	 * parentWorker; } else { return null; } }
	 * 
	 * public PipelineWorker getChildPipelineWorker() { if (this.childWorker
	 * instanceof PipelineWorker) { return (PipelineWorker) childWorker; } else {
	 * return null; } }
	 */

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
