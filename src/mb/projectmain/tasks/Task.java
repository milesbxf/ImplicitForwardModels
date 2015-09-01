package mb.projectmain.tasks;

/**
 * A task that can be run as a separate thread and cancelled.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public interface Task extends Runnable {
	
	/**
	 * Causes this task to finish gracefully.
	 */
	public void cancel();
	
	/**
	 * @return custom user information, e.g. progress of task
	 */
	public Object getUserInformation();
	
}
