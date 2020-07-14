package standrews.cs5099.mpp.instructions;

import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.operations.Operation;

public class SequentialOpInstruction implements Instruction {

	private Operation sequentialOp;
	
	public SequentialOpInstruction(Operation<?,?> operation) {
		this.sequentialOp = operation;
		
	}

	@Override
	public <I> Object executeInstruction(I inputParam, Stack<Instruction> instructions,
			List<Stack<Instruction>> childInstructions) {
		
		//return sequentialOp.execute(inputParam);
		try {
			return sequentialOp.execute(inputParam);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		}
}
