package standrews.cs5099.mpp.core;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import standrews.cs5099.mpp.skeletons.Skeleton;

/**
 * Main class for the <code>MPP</code> library - this class is responsible for
 * maintaining the core {@link TaskExecutor} (thread pool) and creating a
 * {@link WorkScheduler} (class that creates workers and schedules tasks for
 * execution) for a given {@link Skeleton}.
 * 
 * @author Gursher
 */
public class MPP {

	// Core thread pool used to run workers
	private TaskExecutor taskExecutor;

	// Responsible for executing tasks in a work stealing thread pool
	// private ExecutorService workStealingExecutor;

	// The number of threads in the thread pool
	private int numThreads;

	// Main task queue from which workers pick up tasks
	private PriorityBlockingQueue<Runnable> taskQueue;

	// Default singleton instance
	private static MPP mppSingletonInstance;

	public MPP() {
		// Get number of logical cores
		this(Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Creates a singleton instance of <code>MPP</code>
	 * 
	 * @param numWorkers The maximum number of threads in the {@link TaskExecutor}
	 */
	public MPP(int numThreads) {
		this.numThreads = numThreads;
		taskQueue = new PriorityBlockingQueue<Runnable>();
		// initialise the executor service
		taskExecutor = new TaskExecutor(numThreads, numThreads, 10, TimeUnit.SECONDS, taskQueue);
		// initialise the work stealing executor service
		// workStealingExecutor = Executors.newWorkStealingPool();
		mppSingletonInstance = this;

	}

	/**
	 * Factory method for creating a {@link WorkScheduler} for a given
	 * <code>Skeleton</code>
	 * 
	 * @param <I>      The type of input to the </code>Skeleton</code>
	 * @param <O>      The type of output of the </code>Skeleton</code>
	 * @param skeleton The </code>Skeleton</code> composition as specifed by the
	 *                 user
	 * @return {@link WorkScheduler} The <code>WorkScheduler</code> which creates
	 *         workers as per the <code>Skeleton</code> composition
	 */
	public <I, O> WorkScheduler<I, O> createWorkScheduler(Skeleton<I, O> skeleton) {
		if (null == skeleton) {
			throw new IllegalArgumentException("Skeleton cannot be null!");
		}
		return new WorkScheduler(skeleton, taskExecutor);
	}
	
	
	/**
	 * Signals shutdown of executor service. Non-waiting threads will continue to
	 * run for a minute before the service is forcefully shut down
	 * <table>
	 * <tr><td>Title:</td><td>Interface ExecutorService</td></tr>
	 * <tr><td>Author:</td><td>Oracle and/or its affiliates</td></tr>
	 * <tr><td>Version:</td><td>Java SE 7</td></tr>
	 * <tr><td>URL:</td><td><a href="#{@link}">{@link https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ExecutorService.html}</a></td></tr>
	 * </table>
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
	 * @return The singleton instance of {@link MPP}
	 */
	public static synchronized MPP getMppInstance() {
		if (null == mppSingletonInstance || mppSingletonInstance.taskExecutor.isShutdown()) {
			mppSingletonInstance = new MPP();
		}
		return mppSingletonInstance;
	}

}
