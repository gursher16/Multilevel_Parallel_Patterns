package uk.ac.standrews.cs.mpp.skeletons;

import java.util.concurrent.Future;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.core.WorkScheduler;
import uk.ac.standrews.cs.mpp.instructions.InstructionsBuilder;

/**
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
		MPPSkelLib mpp = MPPSkelLib.getMppInstance();
		WorkScheduler<I, O> workScheduler = mpp.createWorkScheduler(this);
		return workScheduler.createWorkersAndExecute(inputData);
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
