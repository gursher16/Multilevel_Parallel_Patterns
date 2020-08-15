package standrews.cs5099.mpp.skeletons;

import java.util.concurrent.Future;

import standrews.cs5099.mpp.core.MPP;
import standrews.cs5099.mpp.core.WorkScheduler;
import standrews.cs5099.mpp.instructions.InstructionsBuilder;

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
	
	public Class<O> outputType;

	private boolean hasMultipleStages;

	/*
	 * Constructor for exactly two stages (minimum)
	 */
	public <E> PipelineSkeleton(Skeleton<I, E> firstStage, Skeleton<E, O> lastStage, Class<O> outputType) {

		this(null, firstStage, lastStage, outputType);
	}

	/*
	 * Constructor for more than two stages
	 */
	public <E> PipelineSkeleton(Skeleton<?, ?>[] stages, Class<O> outputType) {
		this(stages, null, null, outputType);
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
	public <E> PipelineSkeleton(Skeleton<?, ?>[] stages, Skeleton<I, E> firstStage, Skeleton<E, O> lastStage, Class<O> outputType) {
		this.outputType = outputType;
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
		WorkScheduler<I, O> workScheduler = mpp.createWorkScheduler(this);
		return workScheduler.createWorkersAndExecute(inputParam);
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
	
	@Override
	public Class<O> getOutputType() {
		return outputType;
	}

}
