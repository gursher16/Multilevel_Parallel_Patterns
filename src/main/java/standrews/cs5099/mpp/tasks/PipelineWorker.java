package standrews.cs5099.mpp.tasks;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;

public class PipelineWorker implements Runnable, Comparable<PipelineWorker> {

	// Actual data to be executed by the worker
	private Object data;

	// Parent Worker (pipeline stage)
	private PipelineWorker parentWorker;
	// Root Worker
	private PipelineWorker rootWorker;
	
	private PipelineWorker childWorker;

	// ExecutorService responsible for executing worker
	private TaskExecutor taskExecutor;
	// Future object which holds result of computation
	private TaskFuture taskFuture;
	// Execution to be executed by the Worker
	private Instruction instruction;

	/**
	 * Constructor for Root Worker
	 * 
	 * @param data
	 * @param assignedTaskExecutor
	 */
	public PipelineWorker(ExecutorService taskExecutor, Instruction instruction) {
		
		this.rootWorker = this;
		this.taskExecutor = (TaskExecutor) taskExecutor;
		this.instruction = instruction;
		this.taskFuture = new TaskFuture(this.taskExecutor, this);
		this.childWorker = null;
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
		return (parentWorker == null);
	}

	/**
	 * Method which is invoked by an thread issued by the executor service
	 * 
	 */
	
	@Override
	public void run() {
		Object result = null;
		if(this.isRootPipelineWorker()) {
			System.out.println("Wow root task is being executed");
			// link output of current worker to input of next worker
			this.childWorker.setData(this.taskFuture);
			// invoke next worker
			taskExecutor.execute(this.childWorker);			
			// set result of execution in future
			this.taskFuture.setResult(WorkerService.executePipelineWorker(this));		
			
		}
		else {
			System.out.println("Wow child task is being executed");
			try {
				// block till parent worker gives result
				data = this.parentWorker.getFuture().get();
				// if current worker has child
				if(null!=this.childWorker) {
					// link output of current worker with input of next worker
					this.childWorker.setData(this.taskFuture);
					// invoke next worker
					taskExecutor.execute(this.childWorker);
				}				
				this.taskFuture.setResult(WorkerService.executePipelineWorker(this));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
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
		
	}
	/*
	@Override
	public Object call() throws Exception {
		
	}
	*/
	
	public void setChildWorker(PipelineWorker childWorker) {
		this.childWorker = childWorker;
	}
	
	public PipelineWorker getchildWorker() {
		return this.childWorker;
	}

	public boolean isTaskFinished() {
		/** dummy return value **/
		return true;
	}

	public TaskFuture getFuture() {

		return rootWorker.taskFuture;
	}

	public PipelineWorker getParentWorker() {
		return this.parentWorker;
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

	public PipelineWorker getRootWorker() {
		return rootWorker;
	}

	public void setRootWorker(PipelineWorker rootWorker) {
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

	public void setParentWorker(PipelineWorker parentWorker) {
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

	@Override
	public int compareTo(PipelineWorker o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
