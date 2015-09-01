package mb.projectmain.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.projectmain.experiment.ExperimentTask;
import mb.projectmain.experiment.Inspector;
import mb.projectmain.experiment.inputs.InputProviderTypes;
import mb.projectmain.experiment.params.Parameters;

/**
 * Runs tasks as threads; allows adding new tasks on the fly and
 * running/scheduled tasks to be stopped.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class TaskRunner implements TaskFinishCallback {
	
	private final ExecutorService	exec;
	private final List<TaskFuture>	currentTasks;
	
	private TaskRunner ( ExecutorService exec ) {
		this.exec = exec;
		currentTasks = new ArrayList<>();
	}
	
	/**
	 * Static factory method, generates a new TaskRunner with the specified
	 * number of threads.
	 * 
	 * @param numThreads
	 *            maximum number of concurrent threads in the pool
	 * @return a new TaskRunner
	 */
	public static TaskRunner withThreads( int numThreads ) {
		ExecutorService exec = Executors.newFixedThreadPool( numThreads );
		
		TaskRunner runner = new TaskRunner( exec );
		return runner;
	}
	
	/**
	 * Adds a new task to the schedule. Task will be executed once a thread is
	 * free.
	 * 
	 * @param task
	 *            Task to execute
	 * @return the TaskFuture with this task and generated Future.
	 */
	public TaskFuture addTask( Task task ) {
		if ( task instanceof ExperimentTask ) {
			((ExperimentTask) task).registerTaskFinishCallback( this );
		}
		Future<?> future = exec.submit( task );
		TaskFuture newTask = new TaskFuture( task, future );
		currentTasks.add( newTask );
		return newTask;
	}
	
	/**
	 * Stops the Task specified.
	 * @param taskFuture Task to stop.
	 */
	public void stopTask( TaskFuture taskFuture ) {
		
		if ( !currentTasks.contains( taskFuture ) ) {
			throw new IllegalArgumentException( "Task given is not in current tasks" );
		}
		
		if ( taskFuture.isDone() ) {
			throw new IllegalArgumentException( "Task is already done" );
		}
		
		taskFuture.getTask().cancel();
	}
	
	@Override
	public void finishTask( ExperimentTask task ) {
		try {
			//do two Inspector runs automatically
			Parameters inspectorParams = Parameters.copyFrom( task.getParams() );
			Inspector inspector = new Inspector( "exp", inspectorParams );
			addTask( inspector );
			
			Parameters inspectorParamsStdIn = Parameters.copyFrom( task.getParams() );
			inspectorParamsStdIn.inputProperties.input_provider = InputProviderTypes.FLAT_SINE;
			inspectorParamsStdIn.inputProperties.parameters = new Object[] { 1f / 25f, 1f };
			Inspector inspectorStdIn = new Inspector( "std", inspectorParamsStdIn );
			addTask( inspectorStdIn );
			
		} catch ( IOException | JSONNetworkParseError e ) {
			throw new RuntimeException( e );
		}
		
	}
	
}
