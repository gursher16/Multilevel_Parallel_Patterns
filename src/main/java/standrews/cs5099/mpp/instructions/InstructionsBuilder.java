package standrews.cs5099.mpp.instructions;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

/**
 * Class which navigates a skeleton structure and creates a sequence of
 * instructions
 * 
 * @author Gursher
 *
 */
public class InstructionsBuilder {

	// Stac
	private Stack<Instruction> instructionStack;
	private List<Stack<Instruction>> instructionStackList;
	public InstructionsBuilder() {
		this.instructionStack = new Stack<>();
		this.instructionStackList = null;
	}

	/**
	 * Traverse a PipelineSkeleton
	 * 
	 * @param <I>
	 * @param <O>
	 */
	public <I, O> void traverse(PipelineSkeleton<I, O> pipelineSkeleton) {

		if (pipelineSkeleton.getaHasMultipleStages()) {
			Stack<Instruction> tempStack = new Stack<>();
			// Multiple Stages
			for (Skeleton<?, ?> skel : pipelineSkeleton.getStages()) {

				InstructionsBuilder stageBuilder = new InstructionsBuilder();
				// add elements to temp stack
				instructionStack.addAll(stageBuilder.instructionStack);
			}
			// move first stage to top of stack
			Collections.reverse(tempStack);

		} else {
			// Two stages -- create Builder for each stage
			InstructionsBuilder firstStageBuilder = new InstructionsBuilder();
			InstructionsBuilder lastStageBuilder = new InstructionsBuilder();
			
			//traverse both stages to create instruction stack
			pipelineSkeleton.getFirstStage().buildInstructions(firstStageBuilder);
			pipelineSkeleton.getLastStage().buildInstructions(lastStageBuilder);
			
			//add pipelineEnd instruction 
			
			// add last stage first
			instructionStack.addAll(lastStageBuilder.getInstructionsStack());
			// first stage should be top of the stack
			instructionStack.addAll(firstStageBuilder.getInstructionsStack());
			
			// add pipelineBegin instruction
			
		}
	}
	
	public <I,O> void traverse(SequentialOpSkeleton<I, O> seqOpSkeleton) {
		instructionStack.push(new SequentialOpInstruction(seqOpSkeleton.getSequentialOperation()));
	}
	
	public <I,O> void traverse(FarmSkeleton<I, O> farmSkeleton) {
		
	}
	
	public Stack<Instruction> getInstructionsStack(){
		return instructionStack;
	}
}
