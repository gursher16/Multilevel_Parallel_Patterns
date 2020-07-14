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
	
	// the skeleton to farm
	Skeleton<I, O> targetSkeleton;

	public FarmSkeleton(Skeleton<I, O> targetSkeleton) {
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

}
