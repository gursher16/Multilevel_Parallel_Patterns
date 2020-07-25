package standrews.cs5099.mpp.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.instructions.PipelineInstruction;
import standrews.cs5099.mpp.instructions.SequentialOpInstruction;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.PipelineSkeleton;
import standrews.cs5099.mpp.skeletons.SequentialOpSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class TaskBuilder {
	// private Skeleton<?,?> targetSkeleton;
	// private TaskExecutor taskExecutor;
	// private Stack<Instruction> instructionStack;

	// top level worker
	// private PipelineWorker topWorker;

	// private PipelineWorker lastWorker;

	// private List<Pipe> pipelineTask;

	/*
	 * public TaskBuilder(Skeleton<?,?> targetSkeleton, TaskExecutor taskExecutor,
	 * Stack<Instruction> instructionsStack) { this.targetSkeleton = targetSkeleton;
	 * this.taskExecutor = taskExecutor; this.instructionStack = instructionsStack;
	 * }
	 */

	/********
	 * public List<PipelineWorker> buildPipeline(Object inputParam) { //create root
	 * task boolean firstIteration = false; for(Instruction instruction:
	 * instructionStack) { firstIteration = true;
	 * 
	 * // if instruction is pipeline begin instruction if(instruction instanceof
	 * PipelineInstruction) { if(!((PipelineInstruction)
	 * instruction).getIsPipelineTerminated()) { // remove instruction //
	 * instantiate top level worker if(firstIteration) { topWorker = new
	 * PipelineWorker(taskExecutor, instructionStack.pop()); }//buildPipeline() else
	 * { PipelineWorker worker = new Pipe } }
	 * 
	 * }
	 * 
	 * // sequential op instructions
	 * 
	 * // if instruction is pipeline finish instruction
	 * 
	 * // if instruction is farm instruction
	 * 
	 * 
	 * 
	 * } return null; }
	 **********/

	public static Worker[] createWorkers(Skeleton<?, ?> targetSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack) {
		Worker[] workers = null;
		if (targetSkeleton instanceof PipelineSkeleton) {
			workers = createWorkers((PipelineSkeleton) targetSkeleton, taskExecutor, instructionStack);
		} else if (targetSkeleton instanceof SequentialOpSkeleton) {
			workers = createWorkers((SequentialOpSkeleton) targetSkeleton, taskExecutor, instructionStack);
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
	public static Worker[] createWorkers(PipelineSkeleton<?, ?> targetSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack) {

		int iterCount = 0;
		int stackSize = instructionStack.size();
		PipelineWorker topWorker = null;
		PipelineWorker lastWorker = null;
		List<PipelineWorker> workerList = new ArrayList<>();
		PipelineWorker intermediary = null;

		while (!instructionStack.isEmpty()) {
			iterCount += 1;
			Instruction ins = instructionStack.pop();
			// top level worker
			if (iterCount == 1) {
				topWorker = new PipelineWorker(taskExecutor, ins);
				workerList.add(topWorker);

			} else if (iterCount == stackSize) { /* CREATION OF LAST STAGE */

				if (null == intermediary) { // if pipeline has only two stages
					PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
					worker.setParentWorker(topWorker);
					worker.getParentWorker().setChildWorker(worker);
					worker.priority -= iterCount;
					// worker.setData(worker.getParentWorker().getFuture());
					lastWorker = worker;
					workerList.add(lastWorker);
				} else { // if pipeline has multiple stages
					PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
					worker.setParentWorker(intermediary);
					worker.getParentWorker().setChildWorker(worker);
					worker.priority -= iterCount;
					lastWorker = worker;
					workerList.add(lastWorker);
				}
			}

			else { /* CREATION OF INTERMEDIARY STAGES */
				// create intermediary worker and link to output of top worker
				PipelineWorker worker = new PipelineWorker(taskExecutor, ins);
				if (null != intermediary) {
					worker.setParentWorker(intermediary);
				} else {
					worker.setParentWorker(topWorker);
				}
				// worker.setParentWorker(topWorker);
				worker.getParentWorker().setChildWorker(worker);
				worker.priority -= iterCount;
				// worker.setData(worker.getParentWorker().getFuture());
				intermediary = worker;
				workerList.add(intermediary);
			}

		}
		return workerList.toArray(new PipelineWorker[workerList.size()]);
	}

	/**
	 * Method to build a farm worker that contains instructions which can be used to
	 * create multiple instances of pipeline or simplew workers
	 * 
	 * @param farmSkeleton
	 * @param taskExecutor
	 * @param instructionStack
	 * @return
	 */
	public static Worker[] createWorkers(FarmSkeleton<?, ?> farmSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack) {
		// SimpleWorker[] workers = null;
		Worker[] workers = null;
		workers = new Worker[] { new FarmWorker(taskExecutor, farmSkeleton.getTargetSkeleton(),
				farmSkeleton.getNumberOfWorkers(), instructionStack) };
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
	public static Worker[] createWorkers(SequentialOpSkeleton<?, ?> seqSkeleton, TaskExecutor taskExecutor,
			Stack<Instruction> instructionStack) {
		Stack<Instruction> cloneStack = (Stack<Instruction>) instructionStack.clone();
		Worker[] workers = null;
		workers = new Worker[] { new SimpleWorker(taskExecutor, cloneStack.pop()) };
		return workers;
	}

	/////////////////////////////////////////////
	// method for creation of pipeline instances for farm skeleton

}
