package mb.projectmain.experiment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.ctrnn.layout.CTRNNLayout;
import mb.datalog.DataLogger;
import mb.evolution.CoEvoPopulation;
import mb.projectmain.experiment.inputs.InputProvider;
import mb.projectmain.experiment.params.Parameters;
import mb.projectmain.tasks.Task;
import mb.projectmain.tasks.TaskFinishCallback;

/**
 * Represents a single coevolution experiment that can be scheduled and run
 * concurrently with other experiments as a Task.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class ExperimentTask implements Task {
	
	Logger	LOG	= Logger.getLogger( "ExperimentTask" );
	
	/**
	 * Creates a new ExperimentTask (but does not run it yet).
	 * 
	 * @param params
	 *            Experiment parameters.
	 */
	public ExperimentTask ( Parameters params ) {
		this.params = params;
		this.outputStream = new ByteArrayOutputStream();
		
		new File( params.output_path ).mkdirs();
		
		LOG.setLevel( Level.ALL );
	}
	
	private Parameters					params;
	private float						progress		= 0f;
	private final ByteArrayOutputStream	outputStream;
	private InputProvider				provider;
	private CTRNNLayout					testingLayout;
	private CTRNNLayout					targetLayout;
	private CoEvoPopulation				pop;
	private DataLogger					inputsLogger;
	private DataLogger					modelLogger;
	private TaskFinishCallback			taskFinishCallback;
	
	private volatile boolean			running			= false;
	private volatile boolean			cancelRequested	= false;
	
	/**
	 * Registers a class implementing the TaskFinishCallback interface; when the
	 * GAs have finished running this will notify the registered class.
	 * 
	 * @param callback
	 *            Class to register.
	 */
	public void registerTaskFinishCallback( TaskFinishCallback callback ) {
		this.taskFinishCallback = callback;
	}
	
	/**
	 * Sets up the populations and data loggers.
	 * 
	 * @throws FileNotFoundException
	 *             if the network files aren't found
	 * @throws JSONNetworkParseError
	 *             if the network files aren't valid network files
	 * @throws IOException
	 *             if another IO error occurs
	 */
	private void initialise() throws FileNotFoundException, JSONNetworkParseError, IOException {
		provider = params.inputProperties.getProvider();
		
		testingLayout = CTRNNLayout.fromFileName( params.netPropertiesParams.evolved_network_file );
		targetLayout = CTRNNLayout.fromFileName( params.netPropertiesParams.target_network_file );
		
		InputPhenotype input = new InputPhenotype( provider, testingLayout, params.netPropertiesParams );
		ModelPhenotype model = new ModelPhenotype( testingLayout, targetLayout, provider, params );
		
		pop = new CoEvoPopulation(
				input,
				model,
				params.gaPropertiesParams.population,
				params.gaPropertiesParams.mutation_rate,
				params.gaPropertiesParams.crossover_probability,
				params.gaPropertiesParams.deme_size
				);
		
		inputsLogger = new DataLogger( pop.getInputStats() );
		modelLogger = new DataLogger( pop.getModelStats() );
		
	}
	
	/**
	 * @return whether this task is running
	 */
	public synchronized boolean isRunning() {
		return running;
	}
	
	/**
	 * Starts or stops this task
	 * 
	 * @param running
	 *            whether the task should be running
	 */
	public synchronized void setRunning( boolean running ) {
		this.running = running;
	}
	
	/**
	 * Initialises the populations, and runs the experiments. Fails if cancel
	 * has been requested, or if the task is already running.
	 */
	@Override
	public void run() {
		if ( cancelRequested ) {
			return;
		}
		if ( isRunning() ) {
			throw new IllegalStateException( "Task already running" );
		}
		setRunning( true );
		try {
			LOG.info( Thread.currentThread().getName() + " - initialising" );
			
			initialise();
			
			LOG.info( Thread.currentThread().getName() + " - starting run" );
			
			// for progress calculation
			final float totalNumberOfRuns = params.gaLengthParams.total_runs *
					(params.gaLengthParams.input_generations_per_run + params.gaLengthParams.model_generations_per_run);
			
			float runsCounter = 0;
			
			// loop through runs
			
			for ( int i = 0; i < params.gaLengthParams.total_runs && isRunning(); i++ ) {
				
				// loop through specified input evolution runs
				for ( int inputRun = 0; inputRun < params.gaLengthParams.input_generations_per_run && isRunning(); inputRun++ ) {
					pop.doInputGeneration();
					inputsLogger.appendData();
					LOG.info( Thread.currentThread().getName() + "\tinput\t" + pop.getInputStats().getConsoleOutputLine() );
					runsCounter++;
					progress = runsCounter / totalNumberOfRuns;
				}
				
				// loop through specified model evolution runs
				for ( int modelRun = 0; modelRun < params.gaLengthParams.model_generations_per_run && isRunning(); modelRun++ ) {
					pop.doModelGeneration();
					modelLogger.appendData();
					
					LOG.info( Thread.currentThread().getName() + "\tmodel\t" + pop.getModelStats().getConsoleOutputLine() );
					runsCounter++;
					progress = runsCounter / totalNumberOfRuns;
				}
				
			}
			finish(); // clean up
		} catch ( Exception e ) {
			LOG.log( Level.SEVERE, Thread.currentThread().getName() + "Exception", e );
			e.printStackTrace();
		}
	}
	
	/**
	 * cleans up streams, etc. and saves
	 */
	private void finish() {
		try {
			setRunning( false );
			outputStream.close();
			save( params.output_path );
			if ( taskFinishCallback != null ) {
				taskFinishCallback.finishTask( this );
			}
		} catch ( IOException e ) {
			LOG.log( Level.SEVERE, Thread.currentThread().getName() + "IOException after attempting to finish", e );
			throw new RuntimeException( e );
		}
	}
	
	/**
	 * Attempts to stop the experiment.
	 */
	@Override
	public void cancel() {
		setRunning( false );
		cancelRequested = true;
	}
	
	/**
	 * Gets an ExperimentUserInfo object for the GUI with experiment name,
	 * current progress and contents of the outputstream (not working)
	 */
	@Override
	public ExperimentUserInfo getUserInformation() {
		
		return new ExperimentUserInfo( params.name, progress, outputStream.toString() );
	}
	
	/**
	 * @return the Parameters supplied to this experiment.
	 */
	public Parameters getParams() {
		return params;
	}
	
	/**
	 * Saves experiment data, including GA stats and populations, to the
	 * specified directory.
	 * 
	 * @param expDir
	 *            Directory to save to. It will be created if it doesn't exist.
	 * @throws IOException
	 *             if an IO error occurs.
	 */
	public void save( String expDir ) throws IOException {
		String date = new SimpleDateFormat( "dd-MM-yy HHmmss" ).format( new Date() );
		String dir = expDir + "/";
		
		String gaDir = dir + "GA/", networkDir = dir + "networks/";
		
		File gaDirFile = new File( gaDir ), netDirFile = new File( networkDir );
		
		if ( !gaDirFile.exists() )
			gaDirFile.mkdirs();
		
		if ( !netDirFile.exists() )
			netDirFile.mkdirs();
		
		String gaInputStats = gaDir + "inputStats" + date + ".csv", pop1 = gaDir + "inputPop" + date + ".csv", gaModelStats =
				gaDir + "modelStats" + date + ".csv", pop2 = gaDir + "modelPop" + date + ".csv";
		
		inputsLogger.writeToCSVFile( new File( gaInputStats ) );
		modelLogger.writeToCSVFile( new File( gaModelStats ) );
		pop.savePopToCSV( new File( pop1 ), new File( pop2 ) );
		
		java.nio.file.Files
				.copy( new File( params.netPropertiesParams.evolved_network_file ).toPath(), new File( networkDir + "evolvednetwork.json" ).toPath() );
		java.nio.file.Files.copy( new File( params.netPropertiesParams.target_network_file ).toPath(), new File( networkDir + "fixednetwork.json" ).toPath() );
		
		params.writeToFile( dir + "parameters.txt" );
		
	}
	
	/**
	 * Stores information about the state of the experiment, for GUI feedback.
	 * 
	 * @author Miles Bryant <mb459@sussex.ac.uk>
	 *
	 */
	public static class ExperimentUserInfo {
		/**
		 * @param name Name of the experiment.
		 * @param progress Current progress between 0=not started and 1=done.
		 * @param sysOut Output of the experiment.
		 */
		public ExperimentUserInfo ( String name, float progress, String sysOut ) {
			this.name = name;
			this.progress = progress;
			this.sysOut = sysOut;
		}
		
		public final String	name;
		public final float	progress;
		public final String	sysOut;
	}
}
