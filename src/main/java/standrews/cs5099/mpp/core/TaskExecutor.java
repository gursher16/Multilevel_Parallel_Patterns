package standrews.cs5099.mpp.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import standrews.cs5099.mpp.workers.PipelineWorker;
import standrews.cs5099.mpp.workers.Worker;
/**
 * Wrapper class for configuring behaviour of the ExecutorService
 * 
 * @author Gursher
 *
 */
public class TaskExecutor extends ThreadPoolExecutor {

	/**
	 * 
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 */
	
	private boolean exitNow;
	
	TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		exitNow = false;
		// TODO Auto-generated constructor stub
	}
	
	public void setExitNow() {
		exitNow = true;
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(exitNow) {
			this.shutdownNow();			
		}		
	}

}
