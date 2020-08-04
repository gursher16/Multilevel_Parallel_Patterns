package standrews.cs5099.mpp.tasks;

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

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

/**
 * Class responsible for creating tasks and scheduling them for execution. Each
 * {@link Skeleton} has a <code>TaskScheduler</code> associated with it
 * 
 * @author Gursher
 *
 */
public class TaskScheduler<I, O> {

	private TaskExecutor taskExecutor;
	private Skeleton<I, O> targetSkeleton;

	public TaskScheduler(Skeleton<I, O> targetSkeleton, TaskExecutor taskExecutor) {
		this.targetSkeleton = targetSkeleton;
		this.taskExecutor = taskExecutor;
	}

	
	
	/*************/
	public Future<O> scheduleNewTaskForExecution(I inputParam) {
		
		List<PipelineWorker> taskList = new ArrayList<>();
		InstructionsBuilder instructionsBuilder = new InstructionsBuilder();
		targetSkeleton.buildInstructions(instructionsBuilder);
		//TaskBuilder taskBuilder = new TaskBuilder(targetSkeleton, taskExecutor, instructionsBuilder.getInstructionsStack());
		Worker[] workers = TaskBuilder.createWorkers(targetSkeleton, taskExecutor, instructionsBuilder.getInstructionsStack());
		//SimpleWorker [] workers = taskBuilder.buildSkeleton((FarmSkeleton)targetSkeleton);
		RootWorker<O> skeletonWorker = new RootWorker<>(inputParam, taskExecutor, workers, targetSkeleton.getOutputType());
		this.taskExecutor.execute(skeletonWorker);
		return skeletonWorker.getSkelFuture();		
	}
	

}
