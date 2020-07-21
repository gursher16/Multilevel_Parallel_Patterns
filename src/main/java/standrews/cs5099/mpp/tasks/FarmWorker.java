package standrews.cs5099.mpp.tasks;

import java.util.concurrent.ExecutorService;

import standrews.cs5099.mpp.core.TaskExecutor;
import standrews.cs5099.mpp.core.WorkerService;
import standrews.cs5099.mpp.instructions.Instruction;

public class FarmWorker extends Worker{

	
	// Actual data to be executed by the worker
		private Object data;

		// Root Worker
		private RootWorker rootWorker;

		// ExecutorService responsible for executing worker
		private TaskExecutor taskExecutor;
		// Future object which holds result of computation
		private TaskFuture taskFuture;
		// Instruction to be executed by the Worker
		private Instruction instruction;
		
		// Queue<Object> outputQueue;

		/**
		 * Constructor for Root Worker
		 * 
		 * @param data
		 * @param assignedTaskExecutor
		 */
		public FarmWorker(ExecutorService taskExecutor, Instruction instruction) {

			// this.rootWorker = this;
			this.taskExecutor = (TaskExecutor) taskExecutor;
			this.instruction = instruction;
			this.taskFuture = new TaskFuture(this.taskExecutor, this);
			this.priority = 10000;
			this.isFinished = false;
			
			}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (null != inputQueue.peek()) {
				this.data = inputQueue.remove();
				//taskExecutor.execute(command);
				this.outputQueue.add(WorkerService.executeInstruction(data,instruction));
				// this.outputQueue.add(result);
				// invoke next worker
				taskExecutor.execute(this);

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
