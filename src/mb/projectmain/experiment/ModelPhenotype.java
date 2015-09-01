package mb.projectmain.experiment;

import java.util.List;

import mb.ctrnn.core.CTRNN;
import mb.ctrnn.layout.CTRNNLayout;
import mb.evolution.CoEvoPhenotype;
import mb.evolution.Individual;
import mb.projectmain.experiment.inputs.InputProvider;
import mb.projectmain.experiment.params.Parameters;

/**
 * Implements fitness functions for models.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk> *
 */
public class ModelPhenotype implements CoEvoPhenotype {
	
	/**
	 * Creates a new ModelPhenotype with the specified network layouts,
	 * InputProvider and Parameters.
	 * 
	 * @param testingLayout
	 *            Evolvable layout used for candidate models.
	 * @param targetLayout
	 *            Fixed layout used as a target.
	 * @param input
	 *            InputProvider to provide input to networks.
	 * @param params
	 *            Parameters object. This class only uses
	 *            NUM_DERIVS,RUN_LENGTH,INPUT_INDEX,OUTPUT_INDEX and TIMESTEP.
	 */
	public ModelPhenotype ( CTRNNLayout testingLayout, CTRNNLayout targetLayout, InputProvider input, Parameters params ) {
		// set parameters
		this.testingLayout = testingLayout;
		this.targetLayout = targetLayout;
		this.input = input;
		this.NUM_DERIVS = params.modelProperties.derivative_depth;
		this.RUN_LENGTH = params.netPropertiesParams.run_length_steps;
		this.INPUT_INDEX = params.netPropertiesParams.network_input_index;
		this.OUTPUT_INDEX = params.netPropertiesParams.network_output_index;
		this.TIMESTEP = params.netPropertiesParams.time_step;
		
	}
	
	private final CTRNNLayout	testingLayout, targetLayout;	// layouts
	private final InputProvider	input;							// input to
																// networks
																
	private final int			NUM_DERIVS, // number of derivatives to
											// calculate generalized coordinates
											// to
			RUN_LENGTH, // number of timesteps to run network for
			INPUT_INDEX, // index of neuron to provide input to
			OUTPUT_INDEX;										// index of
																// neuron to
																// record output
																// from
	private final float			TIMESTEP;						// timestep for
																// Euler
																// integration
																
	/**
	 * Calculates model fitness for a particular set of genes, with the given
	 * set of InputProvider (tests) individuals.
	 * 
	 * This calculates the mean of the generalized coordinates similarity metric
	 * between the evolved network with the given genes, and the given target
	 * network on every single set of input parameters. See (in the source code)
	 * the private doRun() method for more detailed information.
	 * 
	 * @param individuals
	 *            List of InputProvider individuals. Genes will be taken from
	 *            these as InputProvider parameters
	 * @param genes
	 *            float array of genes for the model.
	 * @returns a scalar float with the fitness of the given set of genes and
	 *          tests individuals, with 1 being a perfect score and lower values
	 *          being worse.
	 */
	@Override
	public float calculateFitness( List<Individual> individuals, float[] genes ) {
		
		// update evolved network
		testingLayout.updateGenes( genes );
		
		// sum up fitness scores
		int nPop = individuals.size();
		float fitSum = 0f;
		
		for ( int r = 0; r < nPop; r++ ) {
			fitSum += doRun( individuals.get( r ).getGenotype().getGenes() );
		}
		
		// return mean of fitness scores
		return fitSum / nPop;
	}
	
	/**
	 * Calculates the generalized coordinates similarity metric between an
	 * evolved and a target network, with the given input parameters.
	 * 
	 * The temporal derivatives are numerically calculated between each
	 * timestep. These are not mathematically/analytically correct but
	 * approximate enough to provide a strong similarity metric. The sum of the
	 * derivative error is then returned.
	 * 
	 * @param inputParameters
	 *            float array of input parameters. Length must match the number
	 *            of parameters for the InputProvider specified when
	 *            constructing this ModelPhenotype.
	 * @return a scalar float with generalized coordinates similarity metric.
	 */
	private float doRun( float[] inputParameters ) {
		
		// create networks. Evolved/testing layout should have had genes updated
		// already.
		CTRNN testing = testingLayout.createCTRNN(), target = targetLayout.createCTRNN();
		
		// set up input with specified parameters.
		input.setParams( inputParameters );
		
		// A and B refer to the evolved and target networks, respectively.
		// These arrays hold the calculated derivative, or difference between
		// each timestep.
		float[][] derivsA = new float[NUM_DERIVS + 1][RUN_LENGTH];
		float[][] derivsB = new float[NUM_DERIVS + 1][RUN_LENGTH];
		
		for ( int t = 0; t < RUN_LENGTH; t++ ) {
			
			// creates input array. All values are zero other than the specified
			// index.
			float[] inputsTarget = new float[targetLayout.getNumberOfNeurons()];
			float[] inputsTest = new float[testingLayout.getNumberOfNeurons()];
			
			inputsTarget[INPUT_INDEX] = input.getInput( t );
			inputsTest[INPUT_INDEX] = input.getInput( t );
			
			// Steps through CTRNNS using Euler's method
			testing.step( TIMESTEP, inputsTest );
			target.step( TIMESTEP, inputsTarget );
			
			// Record the first order
			derivsA[0][t] = testing.getOutputs()[OUTPUT_INDEX];
			derivsB[0][t] = target.getOutputs()[OUTPUT_INDEX];
			
			// Calculate all derivatives up to the specified level.
			for ( int d = 1; d <= NUM_DERIVS; d++ ) {
				if ( t > d - 1 ) { // but only if enough timesteps have passed
					calcDerivs( derivsA, t, d );
					calcDerivs( derivsB, t, d );
				}
			}
		}
		
		// then go through and calculate all derivative differences
		float[] derivSums = new float[NUM_DERIVS];
		
		for ( int i = 0; i < RUN_LENGTH; i++ ) {
			for ( int d = 0; d < NUM_DERIVS; d++ ) {
				// this is the core of the similarity metric, e.g. d^2 * |dA-dB|
				float diff = (float) ((d * d) * Math.abs( derivsA[d][i] - derivsB[d][i] ));
				
				if ( diff < 0 ) { // shouldn't happen
					diff = 0;
				}
				derivSums[d] += diff;
				
				if ( Float.isNaN( diff ) ) {
					throw new AssertionError();
				}
				
			}
		}
		
		float fitSum = 0;
		// go through and sum the differences
		for ( int d = 0; d < NUM_DERIVS; d++ ) {
			fitSum += derivSums[d];
		}
		if ( fitSum < 0 || Float.isNaN( fitSum ) ) {
			throw new AssertionError();
		}
		return 1f - fitSum;
	}
	
	/**
	 * The hidden core of numerical derivative calculation. Takes values from
	 * and outputs to the specified array.
	 * 
	 * @param arr Array for input/output.
	 * @param t Timestep.
	 * @param d Derivative level.
	 */
	private void calcDerivs( float[][] arr, int t, int d ) {
		//this is an obscure method created through refactoring repetitive code.
		//it has been thoroughly tested
		
		int timeStep = t - d;
		
		float result = arr[d - 1][timeStep + 1] - arr[d - 1][timeStep];
		
		if ( Float.isNaN( result ) ) {
			throw new AssertionError();
		}
		
		arr[d][timeStep] = result;
	}
	
	@Override
	public int getGenotypeLength() {
		// TODO Auto-generated method stub
		return testingLayout.getNumberOfGenes();
	}
	
}
