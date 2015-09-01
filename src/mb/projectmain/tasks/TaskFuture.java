package mb.projectmain.tasks;

import java.util.concurrent.Future;

/**
 * Data structure storing both a Task and its corresponding Future.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class TaskFuture {
	
	/**
	 * @param future
	 *            Future to set.
	 * @param task
	 *            Task to set.
	 */
	public TaskFuture ( Task task, Future<?> future ) {
		this.future = future;
		this.task = task;
	}
	
	private final Future<?>	future;
	private final Task		task;
	
	/**
	 * @return the Task.
	 */
	public Task getTask() {
		return task;
	}
	
	/**
	 * @return the Future.
	 */
	public Future<?> getFuture() {
		return future;
	}
	
	/**
	 * @return whether the task is done.
	 */
	public boolean isDone() {
		// TODO Auto-generated method stub
		return future.isDone();
	}
	
}