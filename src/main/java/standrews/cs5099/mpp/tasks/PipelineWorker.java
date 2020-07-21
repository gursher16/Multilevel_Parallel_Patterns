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

public class PipelineWorker<O> extends Worker{

	// Actual data to be executed by the worker
	private Object data;

	// Parent Worker (pipeline stage)
	//private Worker parentWorker;
	// Root Worker
	private RootWorker rootWorker;
	
	//private Worker childWorker;

	
	// Execution to be executed by the Worker
	private Instruction instruction;
	
	//out
	
			

	/**
	 * Constructor for Root Worker
	 * 
	 * @param data
	 * @param assignedTaskExecutor
	 */
	public PipelineWorker(ExecutorService taskExecutor, Instruction instruction) {
		
		//this.rootWorker = this;
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
		//this.rootWorker= parent;
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
	public  void run() {
		
		if(this.isRootPipelineWorker()) {
			while(!this.inputQueue.isEmpty()) {
				this.data = inputQueue.remove();
				System.out.println("Parent worker is being executed");
				// link output of current worker to input of next worker
				// this.childWorker.setData(this.taskFuture);
				
				// set result of execution in future
				Object result = WorkerService.executePipelineWorker(this);
				this.outputQueue.add(result);
				// invoke next worker
				taskExecutor.execute(this.childWorker);
				
			}
			this.outputQueue.add(new String("END"));
			taskExecutor.execute(this.childWorker);
			// set finish message in result to signal successful completion
			//this.taskFuture.setResult("FINISH");
			
		} else {
			System.out.println("Child Worker is being executed");
			try {
				// block till parent worker gives result
				//this.parentWorker.getFuture().get();
				// data = this.parentWorker.outputQueue.remove();
				// free parent worker's future once result is obtained
				// this.parentWorker.getFuture().setResult(null);
				// if current worker has child
				if (null != this.getChildPipelineWorker()) { /** CURRENT WORKER IS INTERMEDIATE WORKER IN PIPELINE **/
					synchronized(this) {
					while (null!=this.getParentWorker().outputQueue.peek()) {
						// link output of current worker with input of next worker
						// this.childWorker.setData(this.taskFuture);
						this.data = this.getParentWorker().outputQueue.remove();						
						// this.taskFuture.setResult(WorkerService.executePipelineWorker(this));
						// invoke next worker
						if(data.equals("END")) {
							System.out.println("ENDING - INTERMEDIATE !!!!");
							this.outputQueue.add(data);
							taskExecutor.execute(this.childWorker);
							break;
						}
						this.outputQueue.add(WorkerService.executePipelineWorker(this));
						taskExecutor.execute(this.childWorker);
						}
					}
				} else { /* CURRENT WORKER IS LAST WORKER IN PIPELINE */
					// call special method which executes and stores result in a Collection
					// WorkerService.executeAndCollectResult(this);
					synchronized(this) {
					while (null!= this.getParentWorker().outputQueue.peek()) {
						this.data = this.getParentWorker().outputQueue.remove();
						if(data.equals("END")) {
							System.out.println("ENDING PIPELINE !!!!!");
							this.taskFuture.setResult("FINISH");
							break;
						}
						
						//** THIS LINE IS WORKING --> WorkerService.executeAndCollectResult(this);**//
						this.outputQueue.add(WorkerService.executePipelineWorker(this));
						// wake up any waiting root worker
						// this.getRootWorker().getFuture().wakeRootWorker();
					} }
					// set future of 
				}
				//this.taskFuture.setResult(WorkerService.executePipelineWorker(this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			//data = this.getParentWorker().getFuture()
		}
		
		//WorkerService.executePipelineTask(this);
		
		/*
		if(rootTask.equals(this)) {
			System.out.println("Root task is being executed");
			WorkerService.pipelineWorker(this);
		}
		*/
		
		
		/**
		 * O result = (O) WorkerService.executeTask(this); return null;
		 **/
		
		//return null;
		this.isFinished = true;
	}
	/*
	@Override
	public Object call() throws Exception {
		
	}
	*/
	
		
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
		
		if(this.parentWorker instanceof PipelineWorker) {
			return (PipelineWorker) parentWorker;
		}
		else {
			return null;
		}
	}
	
	public PipelineWorker getChildPipelineWorker() {
		if(this.childWorker instanceof PipelineWorker) {
			return (PipelineWorker) childWorker;			
		}
		else {
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
		this.rootWorker= rootWorker;
	}
	
	/*
	public List<PipelineWorker> getChildTasks() {
		return childTasks;
	}
	*/
	
	/*
	public void setChildTasks(List<PipelineWorker> childTasks) {
		this.childTasks = childTasks;
	}
	*/
	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setAssignedTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor= taskExecutor;
	}

	public void setParentPipelineWorker(PipelineWorker parentWorker) {
		this.parentWorker= parentWorker;
	}

	public synchronized boolean areChildTasksFinished() {
		// dummy value
		return true;
	}
	/*
	public synchronized boolean hasChildTasks() {
		return (null != childTasks && childTasks.size() > 0);
	}
	*/
	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction instruction) {
		this.instruction= instruction;
	}
	
}
