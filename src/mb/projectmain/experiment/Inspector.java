package mb.projectmain.experiment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import mb.ctrnn.core.LoggableCTRNN;
import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.ctrnn.layout.CTRNNLayout;
import mb.datalog.DataLogger;
import mb.projectmain.experiment.inputs.InputProvider;
import mb.projectmain.experiment.params.Parameters;
import mb.projectmain.tasks.Task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;

/**
 * Used to compare the best network of a particular GA run with the target
 * network, using the best input created by the same run.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class Inspector implements Task {
	
	private static final int	RANK_SELECTION	= 1;								// rank
																					// of
																					// population
																					// to
																					// retrieve,
																					// e.g.
																					// 1
																					// gets
																					// the
																					// highest
																					// fitness
																					// individual
	private String				runName;											// name
																					// to
																					// save
																					// with
	private static Logger		LOG				= Logger.getLogger( "Inspector" );
	
	/**
	 * Creates a new Inspector with the specified parameters. Loads networks and
	 * parameters from the output directory specified in the Parameters object.
	 * 
	 * @param runName
	 *            The name of this run; will be appended to the file saved.
	 * @param params
	 *            Parameters to use.
	 * @throws JsonProcessingException
	 *             If the JSON network file cannot be loaded.
	 * @throws IOException
	 *             If another IO error occurs.
	 * @throws JSONNetworkParseError
	 *             If the JSON network file is syntactically correct but not a
	 *             valid network file.
	 */
	public Inspector ( String runName, Parameters params ) throws JsonProcessingException, IOException, JSONNetworkParseError {
		this.params = params;
		this.runName = runName;
		
		File dir = new File( params.output_path );
		
		LOG.info( Thread.currentThread().getName() + " - initialising Inspector" );
		LOG.info( Thread.currentThread().getName() + " - attempting to load from " + dir.getAbsolutePath() );
		
		// First look for all the files we need.
		
		// these are the files we will look for
		File inputPop = null, modelPop = null, evolvedNet = null, fixedNet = null;
		
		for ( File child : dir.listFiles() ) {
			if ( child.getName().startsWith( "GA" ) ) { // first look for a GA
														// folder
				
				for ( File secondChild : child.listFiles() ) { // then look for
																// the
																// population
																// files
					if ( secondChild.getName().startsWith( "inputPop" ) ) {
						inputPop = secondChild;
					} else if ( secondChild.getName().startsWith( "modelPop" ) ) {
						modelPop = secondChild;
					}
				}
			} else if ( child.getName().startsWith( "networks" ) ) { // then
																		// look
																		// for a
																		// networks
																		// folder
				
				for ( File secondChild : child.listFiles() ) {
					if ( secondChild.getName().startsWith( "evolvednetwork" ) ) {
						evolvedNet = secondChild;
					} else if ( secondChild.getName().startsWith( "fixednetwork" ) ) {
						fixedNet = secondChild;
					}
				}
			}
		}
		
		// if any of the files weren't found, they will be null and we'll need
		// to throw an exception
		if ( inputPop == null || modelPop == null || evolvedNet == null || fixedNet == null ) {
			throw new FileNotFoundException( "Invalid experiment directory selected; does not contain files starting with inputPop and modelPop" );
		}
		
		// load genes from the file with the specified rank
		float[] inputGenes = loadGenes( inputPop.getAbsolutePath(), RANK_SELECTION ), modelGenes = loadGenes( modelPop.getAbsolutePath(), RANK_SELECTION );
		
		// set up networks, input, parameters etc
		
		evolvedLayout = CTRNNLayout.fromFileName( evolvedNet.getAbsolutePath() );
		fixedLayout = CTRNNLayout.fromFileName( fixedNet.getAbsolutePath() );
		
		provider = params.inputProperties.getProvider();
		provider.setParams( inputGenes );
		evolvedLayout.updateGenes( modelGenes );
		
		evolvedCTRNN = new LoggableCTRNN( evolvedLayout.createCTRNN() );
		fixedCTRNN = new LoggableCTRNN( fixedLayout.createCTRNN() );
		
		fixedLogger = new DataLogger( fixedCTRNN );
		evolvedLogger = new DataLogger( evolvedCTRNN );
		
		LOG.info( Thread.currentThread().getName() + " - inspector initialised" );
		
	}
	
	private final Parameters	params;
	
	private CTRNNLayout			fixedLayout;
	private CTRNNLayout			evolvedLayout;
	
	private LoggableCTRNN		fixedCTRNN;
	private LoggableCTRNN		evolvedCTRNN;
	
	private DataLogger			fixedLogger;
	private DataLogger			evolvedLogger;
	
	private InputProvider		provider;
	
	/**
	 * Runs the Inspector and saves all neuron input and output to the specified
	 * file.
	 */
	@Override
	public void run() {
		LOG.info( Thread.currentThread().getName() + " - running Inspector" );
		
		// loop through time steps
		for ( int t = 0; t < params.netPropertiesParams.run_length_steps; t++ ) {
			float[] input = new float[fixedLayout.getNumberOfNeurons()];
			input[params.netPropertiesParams.network_input_index] = provider.getInput( t );
			
			fixedCTRNN.step( params.netPropertiesParams.time_step, input );
			evolvedCTRNN.step( params.netPropertiesParams.time_step, input );
			
			fixedLogger.appendData();
			evolvedLogger.appendData();
		}
		
		String date = new SimpleDateFormat( "dd-MM-yy HHmmss" ).format( new Date() );
		
		File outputDir = new File( params.output_path + "/inspector/" );
		
		LOG.info( Thread.currentThread().getName() + " - finished Inspector, saving data to " + outputDir );
		
		if ( !outputDir.exists() )
			outputDir.mkdirs();
		
		try {
			fixedLogger.writeToCSVFile( new File( outputDir.getAbsolutePath() + "/fixedLOG_" + runName + "_" + date + ".csv" ) );
			evolvedLogger.writeToCSVFile( new File( outputDir.getAbsolutePath() + "/evolvedLOG_" + runName + "_" + date + ".csv" ) );
		} catch ( IOException e ) {
			throw new RuntimeException( e );
		}
	}
	
	@Override
	public void cancel() {
		// inspector runs are generally so short there's not much point adding
		// code to cancel them
	}
	
	@Override
	public Object getUserInformation() {
		// no useful user information to supply
		return null;
	}
	
	/**
	 * Loads a set of genes from a given population CSV file with the specified
	 * fitness rank.
	 * 
	 * @param filename
	 *            CSV file with population data.
	 * @param rank
	 *            Rank of individual to retrieve, e.g. 1 has the highest fitness
	 * @return a float array of genes
	 * @throws IOException
	 *             if an IO error occurs
	 */
	private static float[] loadGenes( String filename, int rank ) throws IOException {
		
		// first part: load CSV file using JacksonCSV
		CsvMapper mapper = new CsvMapper();
		mapper.enable( CsvParser.Feature.WRAP_AS_ARRAY );
		File csvFile = new File( filename );
		MappingIterator<String[]> it = mapper.reader( String[].class ).readValues( csvFile );
		
		final List<Float> fitnesses = new ArrayList<>();
		List<List<Float>> genotypes = new ArrayList<>();
		
		int index = 0;
		
		final Map<List<Float>, Integer> genoIndexes = new HashMap<>();
		
		// iterate through rows and add entries to fitness and genotypes
		while ( it.hasNext() ) {
			String[] row = it.next();
			
			if ( row[0].equals( "fitness" ) ) { // ignore fitness column
				continue;
			}
			
			fitnesses.add( Float.valueOf( row[0] ) );
			List<Float> geno = new ArrayList<>();
			for ( int i = 1; i < row.length; i++ ) {
				geno.add( Float.valueOf( row[i] ) );
			}
			
			genoIndexes.put( geno, index );
			index++;
			genotypes.add( geno );
		}
		
		// now sort both Lists by fitnesses. Its important that both lists are
		// sorted the same way or we'll mess up which fitness corresponds to
		// which genes
		
		Collections.sort( genotypes, new Comparator<List<Float>>() {
			
			@Override
			public int compare( List<Float> arg0, List<Float> arg1 ) {
				int index1 = genoIndexes.get( arg0 ), index2 = genoIndexes.get( arg1 );
				
				return fitnesses.get( index1 ).compareTo( fitnesses.get( index2 ) );
			}
			
		} );
		Collections.sort( fitnesses );
		
		//get an actual index for the rank, i.e. if 1 this should equal the last element
		rank = fitnesses.size() - rank;
		
		float[] genes = new float[genotypes.get( rank ).size()];
		for ( int i = 0; i < genotypes.get( rank ).size(); i++ ) {
			genes[i] = genotypes.get( rank ).get( i );
		}
		return genes;
	}
	
}
