package standrews.cs5099.mpp.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.tasks.MPPTask;
import standrews.cs5099.mpp.tasks.PipelineWorker;
import standrews.cs5099.mpp.tasks.TaskFuture;
import standrews.cs5099.mpp.tasks.Worker;

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
	public static Collection result;
	public static Object[] taskPool;
	public static int index = 0;
	
	// Future object for the result of the skeleton execution
	public static TaskFuture resultFuture;
	
	
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
	
	public static synchronized Object fetchTask() {
		Object task = taskPool[index];
		index+=1;
		return task;
	}
	
	public static void initializeTaskPool(Object input) {
		if(input instanceof Collection) {
			taskPool = ((Collection)input).toArray();
		}				
	}
	
		
	public static void createInstanceOfResult(Class<?> outputType) {
		if (outputType.equals(ArrayList.class)) {
			result = new ArrayList<Object>();
		} else if (outputType.equals(LinkedList.class)) {
			result = new LinkedList<Object>();
		} else if (outputType.equals(Vector.class)) {
			result = new Vector<Object>();
		} else if (outputType.equals(Stack.class)) {
			result = new Stack<Object>();
		} else if (outputType.equals(PriorityQueue.class)) {
			result = new PriorityQueue<Object>();
		} else if (outputType.equals(ArrayDeque.class)) {
			result = new ArrayDeque<>();
		} else if (outputType.equals(HashSet.class)) {
			result = new HashSet<Object>();
		} else if (outputType.equals(LinkedHashSet.class)) {
			result = new LinkedHashSet<Object>();
		} else if (outputType.equals(TreeSet.class)) {
			result = new TreeSet<Object>();
		}
	}
	
	
	public static Object executePipelineWorker(Worker worker) {
		Object data = worker.getData();
		data = worker.getInstruction().executeInstruction(data, null, null);
		return data;
	}
	
	public static Object executeInstruction(Object data, Instruction instruction) {
		return instruction.executeInstruction(data, null, null);
	}
	
	public static void executeAndCollectResult(Worker worker) {
		Object data = worker.getData();
		data = worker.getInstruction().executeInstruction(data, null, null);
		//collectResult(data);
		//synchronized(worker) {
			result.add(data);
	//	}
		
	}
	
	public static Collection fetchResult(Queue<Object> outputQueue) {
		// null check here
		for(Object o: outputQueue) {
			result.add(o);
		}
		//resultFuture.setResult(result);
		return result;
	}
	
	
	public static void setResultFuture(TaskFuture resultFuture) {
		WorkerService.resultFuture = resultFuture;
	}
	
	/**dummy**/
	private static synchronized void collectResult(Object data) {
		result.add(data);
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
