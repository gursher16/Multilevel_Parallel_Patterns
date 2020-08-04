package standrews.cs5099.mpp.instructions;

import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.operations.Operation;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;

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

	public SeqOpInstruction(Operation<?, ?> operation) {
		this.sequentialOp = operation;

	}

	@Override
	public <I> Object executeInstruction(I inputParam, Stack<Instruction> instructions,
			List<Stack<Instruction>> childInstructions) {

		// return sequentialOp.execute(inputParam);
		try {
			return sequentialOp.execute(inputParam);
		} catch (Exception e) {

			// TODO Auto-generated catch block
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!! exception " + inputParam);
			e.printStackTrace();
		}
		return null;
	}
}
