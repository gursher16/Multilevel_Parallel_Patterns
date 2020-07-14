package standrews.cs5099.mpp.core;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.tasks.MPPTask;

public class PipelineWorker {

	private Object input;
	private boolean isRootWorker;
	private boolean isLastWorker;
	private MPPTask task;
	private TaskExecutor taskExecutor;
	private Instruction instruction;
	private Object out;
	
	public PipelineWorker(Object input, Instruction instruction, TaskExecutor taskExecutor, MPPTask task) {
		this.input = input;
		this.task = task;
		this.instruction = instruction;
	}
	
	
	public Object executeInstruction() {
		Future<?> result = (Future<?>) instruction.executeInstruction(input, null, null);
		return result;
	}
}
