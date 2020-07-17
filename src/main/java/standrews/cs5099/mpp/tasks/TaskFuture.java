package standrews.cs5099.mpp.tasks;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import standrews.cs5099.mpp.core.TaskExecutor;

/**
 * Wrapper Class representing the asynchronous execution of an {@link MPPTask}
 * 
 * @author Gursher
 *
 * @param <V>
 */
public class TaskFuture<V> implements Future<V> {

	private TaskExecutor taskExecutor;
	private PipelineWorker task;
	private RootWorker rootworker;
	private V result;
	private boolean isCancelled;

	public TaskFuture(TaskExecutor taskExecutor, PipelineWorker task) {
		// TODO Auto-generated constructor stub
		this.taskExecutor = taskExecutor;
		this.task = task;
	}
	
	public TaskFuture(TaskExecutor taskExecutor, RootWorker rootWorker) {
		this.taskExecutor = taskExecutor;
		this.rootworker = rootWorker;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Blocks execution till the result of a task is computed
	 */
	@Override
	public synchronized V get() throws InterruptedException, ExecutionException {
		while (null == result && !isCancelled) {
			// pause current thread till result is available or execution is cancelled
			wait();
		}
		return result;
	}

	/**
	 * Checks the status of execution
	 * 
	 * @return
	 */
	public boolean isFinished() {
		// return true if result is available or if task is cancelled
		return (null != result || isCancelled);
	}

	/**
	 * Sets the result of the asynchronous computation
	 * 
	 * @param result
	 */
	public synchronized void setResult(V result) {
		// set result and notify all waiting threads
		this.result = result;
		notifyAll();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}
