package standrews.cs5099.mpp.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;
import standrews.cs5099.mpp.skeletons.FarmSkeleton;
import standrews.cs5099.mpp.skeletons.Skeleton;

public class FarmWorker extends Worker{

	
	// Actual data to be executed by the worker
		private Object data;

		// ExecutorService responsible for executing worker
		private TaskExecutor taskExecutor;
		// Future object which holds result of computation
		//private TaskFuture taskFuture;
		// Instruction to be executed by the Worker
		private Instruction instruction;
		
		
		// stack of instructions to be replicated by farm
		private Stack<Instruction> instructionStack;
		
		// skeleton to be replicated by farm
		private Skeleton<?,?> targetSkeleton;
		// Queue<Object> outputQueue;
		
		private List<TaskFuture> futureList;
		
		
		private int noOfWorkers;

		
		
		public FarmWorker(ExecutorService taskExecutor, Skeleton targetSkeleton, Stack<Instruction> instructionStack) {

			// this.rootWorker = this;
			this.taskExecutor = (TaskExecutor) taskExecutor;
			//this.instruction = instruction;
			this.taskFuture = new TaskFuture(this.taskExecutor, this);
			/**latest fields**/
			this.targetSkeleton = targetSkeleton;
			this.instructionStack = instructionStack;
			this.futureList = new ArrayList<>();
			/****/
			this.priority = 10000;
			this.isFinished = false;
			this.noOfWorkers = ((FarmSkeleton)targetSkeleton).getNumberOfWorkers();
			}
	
			

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
									
			List<Worker[]> farmWorkers = new ArrayList<>();
			for(int i = 0; i<noOfWorkers; i++) {
				// create array of workers (will contain at least two workers if target skeleton is a pipeline)
				Worker[] workers = TaskBuilder.createWorkers(targetSkeleton, taskExecutor, instructionStack);
				farmWorkers.add(workers);
			}
			
			// loop till all tasks are consumed from input queue
			while (null != inputQueue.peek()) {
				this.data = inputQueue.remove();
				//taskExecutor.execute(command);
				Worker[] workers = TaskBuilder.createWorkers(targetSkeleton, taskExecutor, instructionStack);
				workers[0].inputQueue.add(data);
				taskExecutor.execute(workers[0]);
				this.futureList.add(workers[workers.length-1].getFuture());
				//this.outputQueue.addAll(workers[workers.length-1].outputQueue);
				// this.outputQueue.add(result);
				// invoke next worker
			}
			for(TaskFuture future: futureList) {
				Object result = null;
				try {
					result = future.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.outputQueue.add(result);
			}
			// this.outputQueue.add(new String("END"));
			// set success message in result to signal successful completion
			this.taskFuture.setResult("SUCCESS");

		}

		@Override
		public Instruction getInstruction() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object getData() {
			// TODO Auto-generated method stub
			return null;
		}

}
