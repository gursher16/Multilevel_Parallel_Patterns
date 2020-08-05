package standrews.cs5099.mpp.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import standrews.cs5099.mpp.skeletons.Skeleton;

/**
 * Main entry point
 * 
 * @author Gursher
 */
public class MPP {

	// Responsible for executing tasks in a thread pool
	private TaskExecutor taskExecutor;

	// Responsible for executing tasks in a work stealing thread pool
	// private ExecutorService workStealingExecutor;

	// The number of threads in the thread pool
	private int noOfCores;

	// Main task queue from which workers pick up tasks
	private PriorityBlockingQueue<Runnable> taskQueue;

	// Default singleton instance
	private static MPP mppSingletonInstance;

	public MPP() {
		// Get number of logical cores
		this(Runtime.getRuntime().availableProcessors());
	}

	public MPP(int noOfThreads) {
		noOfCores = noOfThreads;
		taskQueue = new PriorityBlockingQueue<Runnable>();
		// initialise the executor service
		taskExecutor = new TaskExecutor(noOfCores, noOfCores, 10, TimeUnit.SECONDS, taskQueue);
		// initialise the work stealing executor service
		// workStealingExecutor = Executors.newWorkStealingPool();
		mppSingletonInstance = this;

	}

	/**
	 * Factory method for creating a {@link WorkScheduler} for a given
	 * <code>Skeleton</code>
	 * 
	 * @param <I>
	 * @param <O>
	 * @param skeleton
	 * @return
	 */
	public <I, O> WorkScheduler<I, O> createTaskScheduler(Skeleton<I, O> skeleton) {
		if (null == skeleton) {
			throw new IllegalArgumentException("Skeleton cannot be null!");
		}
		return new WorkScheduler(skeleton, taskExecutor);
	}

	
	
	/**
	 * Signals shutdown of executor service. Non-waiting threads will continue to
	 * run for a minute before the service is forcefully shut down
	 * </br>Citation:</br>
	 * Title:    Interface ExecutorService</br>
	 * Author:   Oracle and/or its affiliates</br>
	 * Version:  Java SE 7</br>
	 * URL:<a href="#{@link}">{@link https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html}</a></br>
	 */
	public void shutDown() {
		taskExecutor.shutdown();
		try {
			// Wait a while for existing tasks to terminate
			if (!taskExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
				taskExecutor.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!taskExecutor.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			taskExecutor.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Return the singleton instance of MPP
	 * 
	 * @return
	 */
	public static synchronized MPP getMppInstance() {
		if (null == mppSingletonInstance || mppSingletonInstance.taskExecutor.isShutdown()) {
			mppSingletonInstance = new MPP();
		}
		return mppSingletonInstance;
	}

}
