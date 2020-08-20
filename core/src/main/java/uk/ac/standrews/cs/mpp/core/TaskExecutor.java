package uk.ac.standrews.cs.mpp.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Wrapper class for configuring behaviour of the {@link ExecutorService}.
 * 
 * @see ThreadPoolExecutor
 * @author Gursher
 *
 */
public class TaskExecutor extends ThreadPoolExecutor {

	/**
	 * Create the <code>ThreadPoolExecutor</code> with the following parameters
	 * 
	 * @param corePoolSize    The number of threads to keep in the thread pool, even
	 *                        if they are idle
	 * @param maximumPoolSize The maximum number of threads allowed in this thread
	 *                        pool
	 * @param keepAliveTime   The maximum amount of time excess threads over the
	 *                        <code>corePoolSize</code> will wait for tasks before
	 *                        being terminated
	 * @param unit            {@link TimeUnit} for the <code>keepAliveTime</code>
	 *                        parameter
	 * @param workQueue       The queue used for holding tasks to be executed by
	 *                        threads in this <code>ThreadPoolExecutor</code>
	 */
	TaskExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
		if(t!=null) {
			System.out.println("Terminating thread pool due to exception..");
			this.shutdownNow();			
		}
	}

}
