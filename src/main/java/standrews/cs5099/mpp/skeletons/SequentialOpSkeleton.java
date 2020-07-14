package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;
import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.tasks.TaskScheduler;

/**
 * Represents a sequential operation
 * 
 * @author Gursher
 *
 * @param <I>
 * @param <O>
 */
public class SequentialOpSkeleton<I, O> implements Skeleton<I, O> {

	private Operation<I, O> operation;

	public SequentialOpSkeleton(Operation<I, O> operation) {
		this.operation = operation;
	}

	@Override
	public Future<O> submitData(I inputData) {
		MPP mpp = MPP.getMppInstance();
		TaskScheduler<I, O> taskScheduler = mpp.createTaskScheduler(this);
		return taskScheduler.scheduleNewTaskForExecution(inputData);
	}

	@Override
	public void buildInstructions(InstructionsBuilder instructionsBuilder) {
		// TODO Auto-generated method stub
		instructionsBuilder.traverse(this);
	}
	
	public Operation<I, O> getSequentialOperation() {
		return operation;
	}

}
