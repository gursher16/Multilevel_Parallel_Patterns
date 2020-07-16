package standrews.cs5099.mpp.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
		
		if(inputParam instanceof Collection) {
			for(Object O : (Collection)inputParam) {
				workers[0].setData(O);
				this.taskExecutor.submit(workers[0]);				
			}			
		}
		
		
		
		
		
		// create and execute new task for pipelined
		for(int i = 0; i<instructionsBuilder.getInstructionsStack().size(); i++) {
			Instruction ins = instructionsBuilder.getInstructionsStack().get(i);
			//PipelineWorker task = new PipelineWorker(inputParam, taskExecutor, ins);
			//taskList.add(task);
			//this.taskExecutor.execute(task);
		}
		// set tasks in worker service
		WorkerService.listOfTasks = taskList;
		if(inputParam instanceof Collection) {
			System.out.println("YES!!");
			for(Object i : (Collection) inputParam) {
				System.out.println("Yay");
				
			}
		}
		
		//MPPTask task = new MPPTask(inputParam, taskExecutor, instructionsBuilder.getInstructionsStack());
		// submit the task for execution
		//this.taskExecutor.execute(task);
		//return (Future<O>) task.getFuture();
		return null;
	}
	
	/**************/

}
