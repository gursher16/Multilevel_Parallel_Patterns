package standrews.cs5099.mpp.core;

import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;
import standrews.cs5099.mpp.workers.PipelineWorker;
import standrews.cs5099.mpp.workers.RootWorker;
import standrews.cs5099.mpp.workers.Worker;
import standrews.cs5099.mpp.workers.WorkerBuilder;

/**
 * Class responsible for creating tasks and scheduling them for execution. Every
 * {@link Skeleton} composition has a <code>WorkScheduler</code> associated with
 * it
 * 
 * @author Gursher
 *
 */
public class WorkScheduler<I, O> {

	private TaskExecutor taskExecutor;
	private Skeleton<I, O> targetSkeleton;

	public WorkScheduler(Skeleton<I, O> targetSkeleton, TaskExecutor taskExecutor) {
		this.targetSkeleton = targetSkeleton;
		this.taskExecutor = taskExecutor;
	}

	/*************/
	public Future<O> scheduleNewTaskForExecution(I inputParam) {

		List<PipelineWorker> taskList = new ArrayList<>();
		InstructionsBuilder instructionsBuilder = new InstructionsBuilder();
		targetSkeleton.buildInstructions(instructionsBuilder);

		Worker[] workers = WorkerBuilder.createWorkers(targetSkeleton, taskExecutor,
				instructionsBuilder.getInstructionsStack());

		RootWorker<O> skeletonWorker = new RootWorker<>(inputParam, taskExecutor, workers,
				targetSkeleton.getOutputType());
		this.taskExecutor.execute(skeletonWorker);
		return skeletonWorker.getSkelFuture();
	}

}
