package standrews.cs5099.mpp.workers;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.instructions.FarmInstruction;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class WorkerBuilder {

	public static Worker[] createWorkers(Skeleton<?, ?> targetSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack) {
		Worker[] workers = null;
		if (targetSkeleton instanceof PipelineSkeleton) {
			workers = createWorkers((PipelineSkeleton) targetSkeleton, taskExecutor, instructionStack, null);
		} else if (targetSkeleton instanceof SequentialOpSkeleton) {
			workers = createWorkers((SequentialOpSkeleton) targetSkeleton, taskExecutor, instructionStack, null);
		} else {
			workers = createWorkers((FarmSkeleton) targetSkeleton, taskExecutor, instructionStack);
		}
		return workers;
	}
	
	public static Worker[] createWorkers(Skeleton<?, ?> targetSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack, FarmWorker farmWorker) {
		Worker[] workers = null;
		if (targetSkeleton instanceof PipelineSkeleton) {
			workers = createWorkers((PipelineSkeleton) targetSkeleton, taskExecutor, instructionStack, farmWorker);
		} else if (targetSkeleton instanceof SequentialOpSkeleton) {
			workers = createWorkers((SequentialOpSkeleton) targetSkeleton, taskExecutor, instructionStack, farmWorker);
		} else {
			workers = createWorkers((FarmSkeleton) targetSkeleton, taskExecutor, instructionStack);
		}
		return workers;
	}


	/**
	 * Method to build workers in a pipeline
	 * 
	 * @param targetSkeleton
	 * @param taskExecutor
	 * @param instructionStack
	 * @return
	 */
	private static Worker[] createWorkers(PipelineSkeleton<?, ?> targetSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack, FarmWorker farmWorker) {

		int iterCount = 0;
		int stackSize = instructionStack.size();
		Worker topWorker = null;
		Worker lastWorker = null;
		List<Worker> workerList = new ArrayList<>();
		Worker intermediary = null;

		while (!instructionStack.isEmpty()) {
			iterCount += 1;
			Instruction instruction = instructionStack.pop();
			// top level worker
			if (iterCount == 1) {
				// topWorker = new PipelineWorker(taskExecutor, ins);
				topWorker = createWorkerBasedOnInstruction(taskExecutor, instruction);
				workerList.add(topWorker);

			} else if (iterCount == stackSize) { /* CREATION OF LAST STAGE */

				if (null == intermediary) { // if pipeline has only two stages
					// PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
					Worker worker = createWorkerBasedOnInstruction(taskExecutor, instruction);
					worker.setParentWorker(topWorker);
					worker.inputQueue = worker.getParentWorker().outputQueue;
					worker.getParentWorker().setChildWorker(worker);
					worker.priority -= iterCount;
					// set farmWorker for last stage if the pipeline is farmed
					worker.farmWorker=farmWorker;					
					// worker.setData(worker.getParentWorker().getFuture());
					lastWorker = worker;
					workerList.add(lastWorker);
				} else { // if pipeline has multiple stages
					// PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
					Worker worker = createWorkerBasedOnInstruction(taskExecutor, instruction);
					worker.setParentWorker(intermediary);
					worker.inputQueue = worker.getParentWorker().outputQueue;
					worker.getParentWorker().setChildWorker(worker);
					worker.priority -= iterCount;
					// set farmWorker for last stage if the pipeline is farmed
					worker.farmWorker=farmWorker;					
					lastWorker = worker;
					workerList.add(lastWorker);
				}
			}

			else { /* CREATION OF INTERMEDIARY STAGES */
				// create intermediary worker and link to output of top worker
				// PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
				Worker worker = createWorkerBasedOnInstruction(taskExecutor, instruction);
				if (null != intermediary) {
					worker.setParentWorker(intermediary);
					worker.inputQueue = worker.getParentWorker().outputQueue;
				} else {
					worker.setParentWorker(topWorker);
					worker.inputQueue = worker.getParentWorker().outputQueue;
				}
				// worker.setParentWorker(topWorker);
				worker.getParentWorker().setChildWorker(worker);
				worker.priority -= iterCount;
				// worker.setData(worker.getParentWorker().getFuture());
				intermediary = worker;
				workerList.add(intermediary);
			}

		}
		return workerList.toArray(new Worker[workerList.size()]);
	}

	/**
	 * Creates a concrete Worker instance depending upon the type of instruction
	 * 
	 * @param taskExecutor
	 * @param instruction
	 * @return
	 */
	private static Worker createWorkerBasedOnInstruction(TaskExecutor taskExecutor, Instruction instruction) {
		Worker worker;
		if (instruction instanceof FarmInstruction) {
			worker = new FarmWorker(taskExecutor, ((FarmInstruction) instruction).getTargetSkeleton(),
					((FarmInstruction) instruction).getNumberOfWorkers(),
					((FarmInstruction) instruction).getSubInstructionsStack());
		} else {
			worker = new PipelineWorker(taskExecutor, instruction);
		}
		return worker;
	}

	/**
	 * Creates workers if a {@link FarmSkeleton} is at the top of the skeleton
	 * composition
	 * 
	 * @param farmSkeleton
	 * @param taskExecutor
	 * @param instructionStack
	 * @return
	 */
	private static Worker[] createWorkers(FarmSkeleton<?, ?> farmSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack) {
		// SimpleWorker[] workers = null;
		Worker[] workers = null;
		FarmInstruction instruction = (FarmInstruction)instructionStack.pop();
		workers = new Worker[] { new FarmWorker(taskExecutor, farmSkeleton.getTargetSkeleton(),
				farmSkeleton.getNumberOfWorkers(), instruction.getSubInstructionsStack()) };
		return workers;
	}

	/**
	 * Method to build a SimpleWorker
	 * 
	 * @param seqSkeleton
	 * @param taskExecutor
	 * @param instructionStack
	 * @return
	 */
	private static Worker[] createWorkers(SequentialOpSkeleton<?, ?> seqSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack, FarmWorker farmWorker) {
		Stack<Instruction> cloneStack = (Stack<Instruction>) instructionStack.clone();
		Worker[] workers = null;
		workers = new Worker[] { new SimpleWorker(taskExecutor, cloneStack.pop(), farmWorker) };		
		return workers;
	}

	/////////////////////////////////////////////
	// method for creation of pipeline instances for farm skeleton

}
