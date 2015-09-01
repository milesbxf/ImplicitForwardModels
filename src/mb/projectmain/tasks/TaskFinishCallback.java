package mb.projectmain.tasks;

import mb.projectmain.experiment.ExperimentTask;

/**
 * Allows a class to register for a notification that a task has finished.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public interface TaskFinishCallback {
	/**
	 * Notifies the implementing class that the specified task has finished.
	 * 
	 * @param task
	 *            Task that has finished.
	 */
	public void finishTask( ExperimentTask task );
}
