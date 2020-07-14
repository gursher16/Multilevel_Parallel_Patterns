package standrews.cs5099.mpp.tasks;

import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.instructions.Instruction;

public class TaskBuilder {
	
	private TaskExecutor taskExecutor;
	private Stack<Instruction> instructionStack;
	
	private List<PipelineTask> pipelineTask;
	
	
	public TaskBuilder(TaskExecutor taskExecutor, Stack<Instruction> instructionsStack) {
		this.taskExecutor = taskExecutor;
		this.instructionStack = instructionsStack;
	}
	
	public List<PipelineTask> buildTasks(Object inputParam) {
		//create root task
		PipelineTask rootTask = new PipelineTask(inputParam, taskExecutor, null);
		for(Instruction i: instructionStack) {
		//	PipelineTask task = new PipelineTask(data, assignedTaskExecutor, instruction)
		}
		return null;
	}
}
