package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

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

	public FarmSkeleton(Skeleton<I, O> targetSkeleton, Class<O> outputType) {
		// TODO Auto-generated constructor stub
		this.targetSkeleton = targetSkeleton;
	}
	
	
	@Override
	public Future<O> submitData(I inputData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void buildInstructions(InstructionsBuilder instructionsBuilder) {
		instructionsBuilder.traverse(this);

	}
	
	@Override
	public Class<O> getOutputType() {
		return outputType;
	}

}
