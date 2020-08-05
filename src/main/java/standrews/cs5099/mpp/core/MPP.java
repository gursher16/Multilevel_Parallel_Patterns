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
	 * run.
	 */
	public void shutDown() {
		System.out.println("Shutting down worker thread..");
		// taskExecutor.
		taskExecutor.shutdown();

	}

	/**
	 * Signals end of parallel execution. Attempts to shut down any remaining
	 * non-waiting thread.
	 */
	public void shutDownNow() {
		System.out.println("Shutting down thread pool..");
		// taskExecutor.
		taskExecutor.shutdownNow();

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
