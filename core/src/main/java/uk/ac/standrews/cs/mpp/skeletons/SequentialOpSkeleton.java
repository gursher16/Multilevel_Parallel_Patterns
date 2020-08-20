package uk.ac.standrews.cs.mpp.skeletons;

import java.util.concurrent.Future;

import uk.ac.standrews.cs.mpp.core.MPPSkelLib;
import uk.ac.standrews.cs.mpp.core.WorkScheduler;
import uk.ac.standrews.cs.mpp.instructions.InstructionsBuilder;
import uk.ac.standrews.cs.mpp.operations.Operation;

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
	public boolean doSplitOutput;

	public SequentialOpSkeleton(Operation<I, O> operation, Class<O> outputType) {
		this(operation, outputType, false);
	}
	
	public SequentialOpSkeleton(Operation<I,O> operation, Class<O> outputType, boolean doSplitOutput) {
		this.operation = operation;
		this.outputType = outputType;
		this.doSplitOutput = doSplitOutput;
	}

	
	public Future<O> submitData(I inputData) {
		
		MPPSkelLib mpp = MPPSkelLib.getMppInstance();
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
