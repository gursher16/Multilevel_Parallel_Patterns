package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.core.WorkScheduler;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;
import standrews.cs5099.mpp.operations.Operation;

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
	public  Class<O> outputType;

	public SequentialOpSkeleton(Operation<I, O> operation, Class<O> outputType) {
		this.operation = operation;
		this.outputType = outputType;
	}

	
	public Future<O> submitData(I inputData) {
		
		MPP mpp = MPP.getMppInstance();
		WorkScheduler<I, O> workScheduler = mpp.createWorkScheduler(this);
		return workScheduler.createWorkersAndExecute(inputData);
	}

	@Override
	public void buildInstructions(InstructionsBuilder instructionsBuilder) {
		// TODO Auto-generated method stub
		instructionsBuilder.traverse(this);
	}
	
	public Operation<I, O> getSequentialOperation() {
		return operation;
	}
	
	@Override
	public Class<O> getOutputType() {
		return outputType;
	}

}
