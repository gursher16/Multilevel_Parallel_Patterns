package standrews.cs5099.mpp.workers;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.instructions.Instruction;

public abstract class Worker implements Runnable, Comparable<Worker>{
	public int priority;
	public boolean isFinished;
	// Mandatory input and output queue for all types of workers
	
	protected Queue<Object> inputQueue;
	protected Queue<Object> outputQueue;
	protected Worker parentWorker;
	protected Worker childWorker;
	protected TaskFuture taskFuture;
	protected TaskExecutor taskExecutor;
	protected RootWorker rootWorker;
	// used when a worker is farmed 
	protected FarmWorker farmWorker;
	protected boolean isExceptionThrown;
	Worker() {
		// Initialize input.output queues for each worker
		inputQueue = new LinkedBlockingQueue<>();
		outputQueue = new LinkedBlockingQueue<>();
	}
	
	
	
	@Override
	public int compareTo(Worker worker) {
		// TODO Auto-generated method stub
		return this.priority - worker.priority;
	}
	
	public void setParentWorker(Worker parentWorker) {
		this.parentWorker = parentWorker;
	}
	
	public Worker getParentWorker() {
		return this.parentWorker;
	}
	
	public Worker getChildWorker() {
		return this.childWorker;
	}
	
	public void setChildWorker (Worker childWorker) {
		this.childWorker = childWorker;
	}
	
	public TaskFuture getFuture() {
		return taskFuture;
	}
	

	
	public abstract Instruction getInstruction();
	public abstract Object getData();	
	
}
