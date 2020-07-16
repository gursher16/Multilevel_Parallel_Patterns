package standrews.cs5099.mpp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.tasks.MPPTask;
import standrews.cs5099.mpp.tasks.PipelineWorker;

/**
 * Core service which executes an {@link MPPTask} and is also responsible for
 * creating new subtasks
 * 
 * @author Gursher
 *
 */
public class WorkerService {
	/**
	 * Method that executes a given Task
	 */
	
	public static List<PipelineWorker> listOfTasks;
	
	public static Object executeTask(MPPTask task) {
		Object data = task.getData();
		Stack<Instruction> instructionStack = task.getInstructions();

		try {
			// store any child instructions
			List<Stack<Instruction>> childInstructions = new ArrayList<Stack<Instruction>>();

			while (!instructionStack.isEmpty() && childInstructions.size() <= 0) {
				Instruction instruction = instructionStack.pop();
				data = instruction.executeInstruction(data, instructionStack, childInstructions);
				// if(task.is)

			}
			task.setData(data);
		} catch (Exception e) {

		}
		return task;

	}
	
	
	
	
	public static Object executePipelineWorker(PipelineWorker worker) {
		Object data = worker.getData();
		data = worker.getInstruction().executeInstruction(data, null, null);
		return data;
	}
	
	
	
	
	
	/***************** PIPELINEWORKER ****/
	/*
	public static Object executePipelineTask(PipelineWorker task) {
		Object data;
		// Stack<Instruction> instructionStack = task.getInstructions();

		try {
			if (task.isRootTask()) {
				data = task.getData();
				data = task.getInstruction().executeInstruction(data, null, null);
				task.setData(data);
			} else {
				data = task.getParentTask().getFuture().get();
				data= task.getInstruction().executeInstruction(data, null, null);
				task.setData(data);
			}
		} catch (Exception e) {
		}*/
	
	
		/*
		 * try { // store any child instructions List<Stack<Instruction>>
		 * childInstructions = new ArrayList<Stack<Instruction>>();
		 * 
		 * while(!instructionStack.isEmpty() && childInstructions.size()<=0) {
		 * Instruction instruction = instructionStack.pop(); data =
		 * instruction.executeInstruction(data, instructionStack, childInstructions);
		 * //if(task.is)
		 * 
		 * } task.setData(data); } catch(Exception e) {
		 * 
		 * }
		 */
	//	return task;
		
//	}

	/*************** FARM WORKER ***********/

	public static Object farmWorker(MPPTask task) {
		Object data = task.getData();
		Stack<Instruction> instructionStack = task.getInstructions();

		try {
			// store any child instructions
			List<Stack<Instruction>> childInstructions = new ArrayList<Stack<Instruction>>();

			while (!instructionStack.isEmpty() && childInstructions.size() <= 0) {
				Instruction instruction = instructionStack.pop();
				data = instruction.executeInstruction(data, instructionStack, childInstructions);
				// if(task.is)

			}
			task.setData(data);
		} catch (Exception e) {

		}
		return task;

	}

	/***************/

	public static void notifyParent(PipelineWorker task) {

	}

}
