package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.core.WorkScheduler;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;

/**
 * Represents a Farm Skeleton
 * 
 * @author Gursher
 *
 * @param <I>
 * @param <O>
 */
public class FarmSkeleton<I, O> implements Skeleton<I, O> {
	
	
	public Class<O> outputType;
	// the skeleton to farm
	Skeleton<I, O> targetSkeleton;
	int noOfWorkers;

	public FarmSkeleton(Skeleton<I, O> targetSkeleton, int noOfWorkers, Class<O> outputType) {
		// TODO Auto-generated constructor stub
		this.targetSkeleton = targetSkeleton;
		this.noOfWorkers=noOfWorkers;
		this.outputType = outputType;
	}
	
	
	@Override
	public Future<O> submitData(I inputData) {
		MPP mpp = MPP.getMppInstance();
		WorkScheduler<I, O> taskScheduler = mpp.createTaskScheduler(this);
		return taskScheduler.scheduleNewTaskForExecution(inputData);
	}

	@Override
	public void buildInstructions(InstructionsBuilder instructionsBuilder) {
		instructionsBuilder.traverse(this);

	}
	
	@Override
	public Class<O> getOutputType() {
		return outputType;
	}
	
	public Skeleton<?,?> getTargetSkeleton() {
		return targetSkeleton;
	}
	
	public int getNumberOfWorkers() {
		return noOfWorkers;
	}

}
