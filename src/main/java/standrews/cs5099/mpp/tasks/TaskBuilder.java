package standrews.cs5099.mpp.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.instructions.PipelineInstruction;
import standrews.cs5099.mpp.instructions.SequentialOpInstruction;

	
public class TaskBuilder {
	private TaskExecutor taskExecutor;
	private Stack<Instruction> instructionStack;
	// top level worker 
	private PipelineWorker topWorker;
	
	private PipelineWorker lastWorker;
		
	//private List<Pipe> pipelineTask;
	
	
	public TaskBuilder(TaskExecutor taskExecutor, Stack<Instruction> instructionsStack) {
		this.taskExecutor = taskExecutor;
		this.instructionStack = instructionsStack;
	}
	
	
	/********
	public List<PipelineWorker> buildPipeline(Object inputParam) {
		//create root task
		boolean firstIteration = false;
		for(Instruction instruction: instructionStack) {
			firstIteration = true;
			
			// if instruction is pipeline begin instruction
			if(instruction instanceof PipelineInstruction) {
				if(!((PipelineInstruction) instruction).getIsPipelineTerminated()) {
					// remove instruction
					// instantiate top level worker
					if(firstIteration)
						{
						topWorker = new PipelineWorker(taskExecutor, instructionStack.pop());
						}//buildPipeline()
					else {
						PipelineWorker worker = new Pipe
					}
				}
				
			}
			
			// sequential op instructions
			
			// if instruction is pipeline finish instruction
			
			// if instruction is farm instruction


			
		}
		return null;
	}
	**********/
	
	
	
	
	
	public PipelineWorker[] buildSkeleton() {
		/* 			UNCOMMENT HERE
		//create root task
		boolean firstIteration = false;
		for(Instruction instruction: instructionStack) {
			firstIteration = true;
			
			// if instruction is pipeline begin instruction
			if(instruction instanceof PipelineInstruction) {
				if(!((PipelineInstruction) instruction).getIsPipelineTerminated()) {
					// remove instruction
					// instantiate top level worker
					if(firstIteration)
						{
						topWorker = new PipelineWorker(taskExecutor, instruction);
						}//buildPipeline()
					else {
						// means nested pipeline -- link to root task of previous pipeline
						PipelineWorker worker = new PipelineWorker(taskExecutor, instruction);
						worker.setParentWorker(topWorker);
					}
				}
				else {
					// means pipeline is terminating -- link
				}
			}
			else if(instruction instanceof SequentialOpInstruction) {	
				PipelineWorker worker = new PipelineWorker(taskExecutor, instruction);
				
			}
			
			// sequential op instructions
			
			// if instruction is pipeline finish instruction
			
			// if instruction is farm instruction


			
		}
		return null; */
		
		int iterCount=0;
		int stackSize = instructionStack.size();
		List<PipelineWorker> workerList = new ArrayList<>();
		PipelineWorker intermediary = null;
		
		while(!instructionStack.isEmpty()) {
			iterCount+=1;
			Instruction ins = instructionStack.pop();
			// top level worker
			if(iterCount == 1) {
				topWorker = new PipelineWorker(taskExecutor, ins);
				workerList.add(topWorker);
				
			} else if(iterCount == stackSize) { /* CREATION OF LAST STAGE */
				
				if(null == intermediary) {  // if pipeline has only two stages
					PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
					worker.setParentWorker(topWorker);
					worker.getParentWorker().setChildWorker(worker);
					worker.priority-=iterCount;
					//worker.setData(worker.getParentWorker().getFuture());
					lastWorker = worker;
					workerList.add(lastWorker);
				}
				else {  // if pipeline has multiple stages
					PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
					worker.setParentWorker(intermediary);
					worker.getParentWorker().setChildWorker(worker);
					worker.priority-=iterCount;
					lastWorker = worker;
					workerList.add(lastWorker);
				}				
			}
			
			else { /* CREATION OF INTERMEDIARY STAGES */
				// create intermediary worker and link to output of top worker
				PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
				if(null!= intermediary) {
					worker.setParentWorker(intermediary);
				}
				else {
					worker.setParentWorker(topWorker);
				}
				//worker.setParentWorker(topWorker);
				worker.getParentWorker().setChildWorker(worker);
				worker.priority-=iterCount;
				//worker.setData(worker.getParentWorker().getFuture());
				intermediary = worker;
				workerList.add(intermediary);
			}		
			
		}
		return workerList.toArray(new PipelineWorker[workerList.size()]);
	}
	
	
	
	
	
	
	
	public List<PipelineWorker> buildPipeline(Object inputParam, Stack<Instruction> instructionStack) {
		
		return null;
	}
	
	
}
