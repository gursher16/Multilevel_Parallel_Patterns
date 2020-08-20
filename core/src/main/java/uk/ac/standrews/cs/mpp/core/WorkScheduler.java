package uk.ac.standrews.cs.mpp.core;

import java.util.concurrent.Future;

import uk.ac.standrews.cs.mpp.instructions.InstructionsBuilder;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;
import uk.ac.standrews.cs.mpp.workers.RootWorker;
import uk.ac.standrews.cs.mpp.workers.Worker;
import uk.ac.standrews.cs.mpp.workers.WorkerBuilder;

/**
 * Class responsible for creating workers and scheduling tasks for execution.
 * Each {@link Skeleton} composition has a <code>WorkScheduler</code> associated
 * with it
 * 
 * @author Gursher
 *
 * @param <I> The type of input to the <code>Skeleton</code> composition
 * @param <O> The type of output from the <code>Skeleton</code> composition
 */
public class WorkScheduler<I, O> {

	private TaskExecutor taskExecutor;
	private Skeleton<I, O> targetSkeleton;

	/**
	 * Constructs a <code>WorkScheduler</code> with the following parameters
	 * 
	 * @param targetSkeleton The {@link Skeleton} composition specified by the user
	 * @param taskExecutor   The core {@link TaskExecutor} that executes workers on
	 *                       separate threads
	 */
	public WorkScheduler(Skeleton<I, O> targetSkeleton, TaskExecutor taskExecutor) {
		this.targetSkeleton = targetSkeleton;
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Creates workers as per the {@link Skeleton} composition and forwards the
	 * input from the <code>Skeleton</code> composition to the {@link RootWorker}
	 * 
	 * @param inputParam Tasks to be executed by the Workers
	 * @return A {@link Future} object representing asynchronous computation of
	 *         tasks
	 */
	public Future<O> createWorkersAndExecute(I inputParam) {
		try {
			// Build instructions as per target skeleton composition
			InstructionsBuilder instructionsBuilder = new InstructionsBuilder();
			targetSkeleton.buildInstructions(instructionsBuilder);

			// Build workers as per target skeleton composition and instructions
			Worker[] workers = WorkerBuilder.createWorkers(targetSkeleton, taskExecutor,
					instructionsBuilder.getInstructionsStack());

			// Create root worker which schedules tasks to workers
			RootWorker<O> skeletonWorker = new RootWorker<>(inputParam, taskExecutor, workers,
					targetSkeleton.getOutputType());
			this.taskExecutor.execute(skeletonWorker);

			return skeletonWorker.getSkelFuture();
			
		} catch (Exception e) {// Not necessary, only for safety's sake
			e.printStackTrace();
			return null;
		}
	}

}
