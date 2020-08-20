package uk.ac.standrews.cs.mpp.instructions;

import java.util.List;
import java.util.Stack;

public interface Instruction {

	public <I> Object executeInstruction(I inputParam, Stack<Instruction> instructions, List<Stack<Instruction>> childInstructions) throws Exception;
}
