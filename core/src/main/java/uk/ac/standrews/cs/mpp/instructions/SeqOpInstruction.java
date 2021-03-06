package uk.ac.standrews.cs.mpp.instructions;

import java.util.List;
import java.util.Stack;

import uk.ac.standrews.cs.mpp.exceptions.MPPException;
import uk.ac.standrews.cs.mpp.operations.Operation;
import uk.ac.standrews.cs.mpp.skeletons.SequentialOpSkeleton;

/**
 * An internal representation of {@link SequentialOpSkeleton} used for building
 * workers according to the skeleton composition
 * 
 * @author Gursher
 *
 */
public class SeqOpInstruction implements Instruction {

	// The computation, as specified by a user, to be parallelised
	private Operation sequentialOp;
	public boolean doSplitOutput;

	public SeqOpInstruction(Operation<?, ?> operation, boolean doSplitOutput) {
		this.sequentialOp = operation;
		this.doSplitOutput = doSplitOutput;
	}

	@Override
	public <I> Object executeInstruction(I inputParam, Stack<Instruction> instructions,
			List<Stack<Instruction>> childInstructions) throws MPPException {

		try {
			return sequentialOp.execute(inputParam);
		} catch (Exception e) {
			throw new MPPException(e);
		}

	}
}
