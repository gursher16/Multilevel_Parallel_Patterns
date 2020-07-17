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

	/**
	 * Method that submits a task to the executor service for execution
	 * 
	 * @param inputParam
	 * @return
	 */
	public Future<O> scheduleNewTaskForExecution(I inputParam) {

		InstructionsBuilder instructionsBuilder = new InstructionsBuilder();
		targetSkeleton.buildInstructions(instructionsBuilder);
		// create a new task
		MPPTask task = new MPPTask(inputParam, taskExecutor, instructionsBuilder.getInstructionsStack());
		// submit the task for execution
		this.taskExecutor.execute(task);
		return (Future<O>) task.getFuture();
	}
	
	
	/*************/
	public Future<O> scheduleNewTaskForExecutionPipeLine(I inputParam) {
		
		List<PipelineWorker> taskList = new ArrayList<>();
		InstructionsBuilder instructionsBuilder = new InstructionsBuilder();
		targetSkeleton.buildInstructions(instructionsBuilder);
		TaskBuilder taskBuilder = new TaskBuilder(taskExecutor, instructionsBuilder.getInstructionsStack());
		PipelineWorker[] workers = taskBuilder.buildSkeleton();
		RootWorker<O> skeletonWorker = new RootWorker<>(inputParam, taskExecutor, workers, targetSkeleton.getOutputType());
		this.taskExecutor.execute(skeletonWorker);
		return skeletonWorker.getSkelFuture();
		
	}
	/*
	private void createInstanceOfResult() {
		this.getClass().ge
		if (O instanceof ArrayList) {
			result = new ArrayList<Object>();
		} else if (resultType instanceof LinkedList) {
			result = new LinkedList<Object>();
		} else if (resultType instanceof Vector) {
			result = new Vector<Object>();
		} else if (resultType instanceof Stack) {
			result = new Stack<Object>();
		} else if (resultType instanceof PriorityQueue) {
			result = new PriorityQueue<Object>();
		} else if (resultType instanceof ArrayDeque) {
			result = new ArrayDeque<>();
		} else if (resultType instanceof HashSet) {
			result = new HashSet<Object>();
		} else if (resultType instanceof LinkedHashSet) {
			result = new LinkedHashSet<Object>();
		} else if (resultType instanceof TreeSet) {
			result = new TreeSet<Object>();
		}

	}*/
	
	/**************/

}
