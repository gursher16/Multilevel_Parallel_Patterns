package uk.ac.standrews.cs.mpp.instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import uk.ac.standrews.cs.mpp.skeletons.FarmSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.PipelineSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;
import uk.ac.standrews.cs.mpp.skeletons.Skeleton;

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
	// contains list of instructions stacks for skeletons
	private List<Stack<Instruction>> instructionStackList;

	public InstructionsBuilder() {
		this.instructionStack = new Stack<>();
		this.instructionStackList = new ArrayList<>();
	}

	/**
	 * Traverse a PipelineSkeleton and build instructions
	 * 
	 * @param <I>
	 * @param <O>
	 */
	public <I, O> void traverse(PipelineSkeleton<I, O> pipelineSkeleton) {
		/**
		 * // Signals start of pipeline PipelineInstruction pipeLineBegin = new
		 * PipelineInstruction(false); // Signals end of pipeline PipelineInstruction
		 * pipeLineEnd = new PipelineInstruction(true); // link pipeline instructions
		 * pipeLineBegin.linkPipelineInstruction(pipeLineEnd);
		 **/

		if (pipelineSkeleton.getaHasMultipleStages()) {
			Stack<Instruction> tempStack = new Stack<>();
			// Multiple Stages
			for (Skeleton<?, ?> skel : pipelineSkeleton.getStages()) {

				InstructionsBuilder stageBuilder = new InstructionsBuilder();
				skel.buildInstructions(stageBuilder);
				// add elements to temp stack
				tempStack.addAll(stageBuilder.instructionStack);
			}
			// move first stage to top of stack
			Collections.reverse(tempStack);
			instructionStack.addAll(tempStack);

		} else {
			// Two stages -- create Builder for each stage
			InstructionsBuilder firstStageBuilder = new InstructionsBuilder();
			InstructionsBuilder lastStageBuilder = new InstructionsBuilder();

			// push pipeline end instruction
			// instructionStack.push(pipeLineEnd);

			// traverse both stages to create instruction stack
			pipelineSkeleton.getFirstStage().buildInstructions(firstStageBuilder);
			pipelineSkeleton.getLastStage().buildInstructions(lastStageBuilder);

			// add last stage first
			instructionStack.addAll(lastStageBuilder.getInstructionsStack());
			// first stage should be top of the stack
			instructionStack.addAll(firstStageBuilder.getInstructionsStack());
			// push pipeline begin instruction
			// instructionStack.push(pipeLineBegin);

		}
	}

	/**
	 * Push a Sequential Operation Instruction
	 * 
	 * @param <I>
	 * @param <O>
	 * @param seqOpSkeleton
	 */
	public <I, O> void traverse(SequentialOpSkeleton<I, O> seqOpSkeleton) {
		instructionStack
				.push(new SeqOpInstruction(seqOpSkeleton.getSequentialOperation(), seqOpSkeleton.doSplitOutput));
	}

	/**
	 * Push a Farm Instruction
	 * 
	 * @param <I>
	 * @param <O>
	 * @param farmSkeleton
	 */
	public <I, O> void traverse(FarmSkeleton<I, O> farmSkeleton) {
		InstructionsBuilder builder = new InstructionsBuilder();
		farmSkeleton.getTargetSkeleton().buildInstructions(builder);
		FarmInstruction farmInstruction = new FarmInstruction(farmSkeleton.getTargetSkeleton(),
				builder.getInstructionsStack(), farmSkeleton.getNumberOfWorkers());
		instructionStack.push(farmInstruction);
		// instructionStack.addAll(builder.getInstructionsStack());
	}

	public Stack<Instruction> getInstructionsStack() {
		return instructionStack;
	}
}
