package standrews.cs5099.mpp.tasks;

public abstract class Worker implements Runnable, Comparable<Worker>{
	public int priority;
	public boolean isFinished;
	@Override
	public int compareTo(Worker worker) {
		// TODO Auto-generated method stub
		return this.priority - worker.priority;
	}	
	
}
