package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;
import standrews.cs5099.mpp.tasks.TaskScheduler;

/**
 * Represents a Pipeline Skeleton
 * 
 * @author Gursher
 *
 * @param <I>
 * @param <O>
 */
public class PipelineSkeleton<I, O> implements Skeleton<I, O> {

	// first stage of the pipeline
	private Skeleton<I, ?> firstStage;
	// last stage of the pipeline
	private Skeleton<?, O> lastStage;
	// set of stages including first and last stage
	private Skeleton<?, ?>[] stages;

	private boolean hasMultipleStages;

	/*
	 * Constructor for exactly two stages (minimum)
	 */
	public <E> PipelineSkeleton(Skeleton<I, E> firstStage, Skeleton<E, O> lastStage) {

		this(null, firstStage, lastStage);
	}

	/*
	 * Constructor for more than two stages
	 */
	public <E> PipelineSkeleton(Skeleton<I, E>[] stages) {
		this(stages, null, null);
	}

	/**
	 * Main Constructor
	 * 
	 * @param <E>
	 * @param stages
	 * @param firstStage
	 * @param lastStage
	 */
	@SuppressWarnings("unchecked")
	public <E> PipelineSkeleton(Skeleton<?, ?>[] stages, Skeleton<I, E> firstStage, Skeleton<E, O> lastStage) {

		if (null != firstStage && null != lastStage) {
			this.firstStage = firstStage;
			this.lastStage = lastStage;
			this.hasMultipleStages = false;
		}

		if (null != stages) {
			if (stages.length == 1) {
				throw new IllegalArgumentException(
						"Invalid number of stages: please enter more than one pipeline stage");
			} else {
				this.stages = stages;
				this.firstStage = (Skeleton<I, E>) stages[0];
				this.lastStage = (Skeleton<E, O>) stages[stages.length - 1];
				this.hasMultipleStages = true;
			}

		}
	}

	@Override
	public Future<O> submitData(I inputParam) {

		MPP mpp = MPP.getMppInstance();
		TaskScheduler<I, O> taskScheduler = mpp.createTaskScheduler(this);
		return taskScheduler.scheduleNewTaskForExecutionPipeLine(inputParam);
	}

	public boolean getaHasMultipleStages() {
		return this.hasMultipleStages;
	}

	public Skeleton<?, ?>[] getStages() {
		return this.stages;
	}
	
	public Skeleton<?,?> getFirstStage() {
		return firstStage;
	}
	public Skeleton<?,?> getLastStage() {
		return lastStage;
	}
	@Override
	public void buildInstructions(InstructionsBuilder instructionsBuilder) {
		instructionsBuilder.traverse(this);
		
	}

}
