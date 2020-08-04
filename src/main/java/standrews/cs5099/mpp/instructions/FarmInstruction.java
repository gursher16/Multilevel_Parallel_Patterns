package standrews.cs5099.mpp.instructions;

import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class FarmInstruction implements Instruction {

	private Stack<Instruction> targetInstructions;
	private Skeleton targetSkeleton;
	private int numberOfWorkers;
	public FarmInstruction(Skeleton targetSkeleton, Stack<Instruction> targetInstructions, int numberOfWorkers) {
		this.targetInstructions = targetInstructions;
		this.targetSkeleton = targetSkeleton;
		this.numberOfWorkers = numberOfWorkers;
	}
	
	public int getNumberOfWorkers() {
		return numberOfWorkers;
	}
		
	public Stack<Instruction> getTargetInstructions() {
		return targetInstructions;
	}
	
	public Skeleton getTargetSkeleton() {
		return targetSkeleton;
	}

	@Override
	public <I> Object executeInstruction(I inputParam, Stack<Instruction> instructions,
			List<Stack<Instruction>> childInstructions) {
		
		return null;
		
	}

}
