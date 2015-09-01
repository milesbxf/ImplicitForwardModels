package mb.ctrnn.core;

import java.util.ArrayList;
import java.util.List;

import mb.datalog.Loggable;

/**
 * Logs CTRNN inputs and outputs to a mb.datalog.Loggable interface
 * 
 * Extends CTRNN functionality to enable recording of neuron inputs and outputs
 * at each time step, outputting them to a mb.datalog.Loggable interface.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 */
public class LoggableCTRNN extends CTRNN implements Loggable {
	
	/**
	 * Creates a new LoggableCTRNN copying all parameters from the given CTRNN.
	 * 
	 * @param ctrnn CTRNN to copy parameters from.
	 */
	public LoggableCTRNN ( CTRNN ctrnn ) {
		super( ctrnn.n, ctrnn.taus, ctrnn.biases, ctrnn.gains, ctrnn.weights );
	}
	
	private float[]	lastInputs;
	
	/**
	 * Creates a new LoggableCTRNN with the specified parameters.
	 * 
	 * @param n Number of neurons.
	 * @param taus Time constants. One for each neuron.
	 * @param biases Neuronal biases. One for each neuron.
	 * @param gains Neuronal gain. One for each neuron.
	 * @param weights Matrix of weights representing connections between neurons.
	 */
	public LoggableCTRNN ( int n, float[] taus, float[] biases, float[] gains,
			float[][] weights ) {
		super( n, taus, biases, gains, weights );
	}
	
	@Override
	public void step( float stepSize, float[] inputs ) {
		super.step( stepSize, inputs );
		lastInputs = inputs; //record to pass to logger
	}
	
	@Override
	public List<Object> getData() {
		List<Object> data = new ArrayList<>();
		for ( int i = 0; i < getOutputs().length; i++ ) {
			data.add( getOutputs()[i] );
			data.add( lastInputs[i] );
		}
		return data;
	}
	
	@Override
	public List<String> getHeaders() {
		List<String> headers = new ArrayList<>();
		for ( int i = 0; i < getOutputs().length; i++ ) {
			headers.add( "neur" + i );
			headers.add( "input" + i );
		}
		return headers;
	}
	
}
