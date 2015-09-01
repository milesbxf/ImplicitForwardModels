package mb.projectmain.experiment.params;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import mb.projectmain.experiment.inputs.InputProvider;
import mb.projectmain.experiment.inputs.InputProviderTypes;

/**
 * Stores experimental parameters.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class Parameters {
	
	/**
	 * Copy constructor, hard copies all parameters from the given Parameters.
	 * 
	 * @param params
	 *            Parameters object to copy from.
	 * @return A new, unique Parameters object with copied parameters.
	 */
	public static Parameters copyFrom( Parameters params ) {
		Parameters newParams = new Parameters();
		
		newParams.name = params.name;
		newParams.output_path = params.output_path;
		newParams.gaLengthParams.total_runs = params.gaLengthParams.total_runs;
		newParams.gaLengthParams.input_generations_per_run = params.gaLengthParams.input_generations_per_run;
		newParams.gaLengthParams.model_generations_per_run = params.gaLengthParams.model_generations_per_run;
		
		newParams.gaPropertiesParams.population = params.gaPropertiesParams.population;
		newParams.gaPropertiesParams.mutation_rate = params.gaPropertiesParams.mutation_rate;
		newParams.gaPropertiesParams.crossover_probability = params.gaPropertiesParams.crossover_probability;
		newParams.gaPropertiesParams.deme_size = params.gaPropertiesParams.deme_size;
		
		newParams.inputProperties.input_provider = params.inputProperties.input_provider;
		newParams.inputProperties.parameters = Arrays.copyOf( params.inputProperties.parameters, params.inputProperties.parameters.length );
		
		newParams.modelProperties.derivative_depth = params.modelProperties.derivative_depth;
		newParams.modelProperties.exponential_penalty = params.modelProperties.exponential_penalty;
		
		return newParams;
	}
	
	public String				name				= "";							// descriptive
																					// name
																					// of
																					// the
																					// experiment
	public String				output_path			= "";							// directory
																					// to
																					// output
																					// to
	public GaLengthParams		gaLengthParams		= new GaLengthParams();
	public GaPropertiesParams	gaPropertiesParams	= new GaPropertiesParams();
	public NetPropertiesParams	netPropertiesParams	= new NetPropertiesParams();
	public InputProperties		inputProperties		= new InputProperties();
	public ModelProperties		modelProperties		= new ModelProperties();
	
	/**
	 * Stores GA length parameters.
	 */
	public static class GaLengthParams {
		public int	total_runs	= 200, // total number of iterations through the
										// main loop
				input_generations_per_run = 1, // iterations of input
												// generations per loop
				model_generations_per_run = 1;	// iterations of model
												// generations per loop
				
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append( "GA Length\n\ttotal_runs=" );
			builder.append( total_runs );
			builder.append( "\n\tinput_generations_per_run=" );
			builder.append( input_generations_per_run );
			builder.append( "\n\tmodel_generations_per_run=" );
			builder.append( model_generations_per_run ).append( "\n" );
			return builder.toString();
		}
		
	}
	
	/**
	 * Stores other GA parameters.
	 */
	public static class GaPropertiesParams {
		public int	population	= 200;	// number of individuals in population
		public float	mutation_rate	= 0.4f, // standard deviation of
												// Gaussian noise to add whilst
												// mutating
				crossover_probability = 0.05f;	// probability of each gene
												// being crossed over
		public int		deme_size		= 5;	// geographical size to select
												// individuals from
												
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append( "GA Properties\n\tpopulation=" );
			builder.append( population );
			builder.append( "\n\tmutation_rate=" );
			builder.append( mutation_rate );
			builder.append( "\n\tcrossover_probability=" );
			builder.append( crossover_probability );
			builder.append( "\n\tdeme_size=" );
			builder.append( deme_size ).append( "\n" );
			return builder.toString();
		}
		
	}
	
	/**
	 * Stores parameters relating to the networks.
	 */
	public static class NetPropertiesParams {
		public String	target_network_file	= "", // target network to model
				evolved_network_file = "";				// network to evolve
														// parameters for as
														// candidate models
		public float	time_step			= 0.01f;	// timestep for Euler
														// integration
		public int		run_length_steps	= 500, // length of time to run
													// CTRNNs for
				network_input_index = 0, // node to provide input to
				network_output_index = 1;				// node to
														// record/compare output
														// from
				
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append( "Net Properties\n\ttarget_network_file=" );
			builder.append( target_network_file );
			builder.append( "\n\tevolved_network_file=" );
			builder.append( evolved_network_file );
			builder.append( "\n\ttime_step=" );
			builder.append( time_step );
			builder.append( "\n\trun_length_steps=" );
			builder.append( run_length_steps );
			builder.append( "\n\tnetwork_input_index=" );
			builder.append( network_input_index );
			builder.append( "\n\tnetwork_output_index=" );
			builder.append( network_output_index ).append( "\n" );
			return builder.toString();
		}
		
	}
	
	/**
	 * Parameters relating to the CTRNN input.
	 */
	public static class InputProperties {
		public InputProviderTypes	input_provider	= InputProviderTypes.SINUSOIDAL;	// enum
																						// of
																						// input
																						// types
		public Object[]				parameters		= new Object[] { 0.25f, 0.2f };	// parameters
																						// for
																						// the
																						// input
																						// type
																						
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append( "Input Properties\n\tinput_provider_classname=" );
			builder.append( input_provider );
			builder.append( "\n\tparameters=" );
			builder.append( Arrays.toString( parameters ) ).append( "\n" );
			return builder.toString();
		}
		
		public InputProvider getProvider() {
			return input_provider.createNewInstance( (Object[]) parameters );
		}
		
	}
	
	public static class ModelProperties {
		public int	derivative_depth	= 5,
										exponential_penalty = 2;
		
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append( "Model Properties\n\tderivative_depth=" );
			builder.append( derivative_depth );
			builder.append( "\n\texponential_penalty=" );
			builder.append( exponential_penalty ).append( "\n" );
			return builder.toString();
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( "Parameters\n\t" );
		sb.append( name ).append( "\n\t" );
		sb.append( output_path ).append( "\n" );
		sb.append( gaLengthParams.toString() );
		sb.append( gaPropertiesParams.toString() );
		sb.append( netPropertiesParams.toString() );
		sb.append( inputProperties.toString() );
		sb.append( modelProperties.toString() ).append( "\n" );
		return sb.toString();
	}
	
	/**
	 * Writes these parameters in a human-readable format to the specified file
	 * 
	 * @param filename
	 *            File to write to
	 * @throws IOException
	 *             if an IO error occurs
	 */
	public void writeToFile( String filename ) throws IOException {
		
		FileWriter writer = new FileWriter( filename );
		writer.write( toString() );
		writer.flush();
		writer.close();
	}
	
}
