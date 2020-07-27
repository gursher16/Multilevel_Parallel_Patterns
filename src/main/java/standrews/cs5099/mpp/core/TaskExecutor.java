package standrews.cs5099.mpp.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import standrews.cs5099.mpp.tasks.PipelineWorker;
import standrews.cs5099.mpp.tasks.Worker;
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
	public TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		this.shutdown();
		//Thread.currentThread().
		/**
		Future result = (Future) r;
		try {
			result.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		**/
		/**
		Worker worker = (Worker)r;
			if(worker.isFinished) {
				//System.out.println("*************");
				//((PipelineWorker) worker).getFuture().setResult(null);
			}
			**/
			
			
		
		
	}

}
