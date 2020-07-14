package standrews.cs5099.mpp.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import standrews.cs5099.mpp.tasks.MPPTask;

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
		/** best practice? **/
		super.afterExecute(r, t);
		MPPTask task = (MPPTask) r;
		if (task.isTaskFinished()) {
			WorkerService.notifyParent(task);
		}

	}

}
