package standrews.cs5099.mpp.instructions;

import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.skeletons.Skeleton;

public class PipelineInstruction implements Instruction {
	
	private boolean isPipelineTerminated;
		
	public PipelineInstruction(boolean isPipelineTerminated) {
		// TODO Auto-generated constructor stub
		this.isPipelineTerminated = isPipelineTerminated;
	}
	
	@Override
	public <I> Object executeInstruction(I inputParam, Stack<Instruction> instructions,
			List<Stack<Instruction>> childInstructions) {
		// TODO Auto-generated method stub
		// does NOTHING
		return null;
	}

}
