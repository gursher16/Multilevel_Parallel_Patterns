package standrews.cs5099.mpp.tasks;

import standrews.cs5099.mpp.instructions.Instruction;

public abstract class Worker implements Runnable, Comparable<Worker>{
	public int priority;
	public boolean isFinished;
	@Override
	public int compareTo(Worker worker) {
		// TODO Auto-generated method stub
		return this.priority - worker.priority;
	}
	public abstract Instruction getInstruction();
	public abstract Object getData();	
	
}
