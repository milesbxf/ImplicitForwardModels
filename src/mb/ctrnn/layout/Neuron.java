package mb.ctrnn.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a single neuron in a CTRNN network, with tau,bias and gain
 * parameters, and connection weights.
 * 
 * @author Miles Bryant
 *
 */
public class Neuron {
	/**
	 * Represents the type of parameter.
	 */
	public static enum ParameterType {
		/**
		 * The time constant, or how rapidly this neuron's output changes in
		 * response to input.
		 */
		TAU,
		/**
		 * The neuronal bias; whether this neuron is excitatory (negative bias)
		 * or inhibitory (positive bias).
		 */
		BIAS,
		/**
		 * Neuronal gain, or amplification of output; typically used in sensory
		 * neurons.
		 */
		GAIN
	}
	
	/**
	 * Creates a new neuron with fixed values (no genes).
	 * 
	 * @param tau
	 * @param bias
	 * @param gain
	 * @param weights
	 * @return a new neuron with fixed values.
	 */
	public static Neuron withFixedValues( float tau, float bias, float gain, float[] weights ) {
		Neuron neuron = new Neuron( weights.length );
		neuron.setParameter( ParameterType.TAU, new NeuronParam.Fixed( tau ) );
		neuron.setParameter( ParameterType.BIAS, new NeuronParam.Fixed( bias ) );
		neuron.setParameter( ParameterType.GAIN, new NeuronParam.Fixed( gain ) );
		neuron.setAllWeightsFixed( weights );
		return neuron;
	}
	
	private Map<ParameterType, NeuronParam>	parameters;
	private List<NeuronParam>				weights;
	
	/**
	 * Instantiates a new blank neuron with no parameters set and connection
	 * strengths initialised to zero.
	 * 
	 * @param noOfNeuronsInNetwork
	 *            Total number of neurons in the network.
	 */
	public Neuron ( int noOfNeuronsInNetwork ) {
		parameters = new HashMap<>();
		weights = new ArrayList<>( noOfNeuronsInNetwork );
		
		// set all weights to zero
		for ( int i = 0; i < noOfNeuronsInNetwork; i++ ) {
			weights.add( new NeuronParam.Fixed( 0f ) );
		}
	}
	
	/**
	 * Gets the float value of a particular parameter. Throws a NullPointer
	 * exception if the parameter has not been set.
	 * 
	 * @param type
	 *            Parameter to get.
	 * @return float value of the parameter. If the parameter is a gene, the
	 *         mapped value is returned.
	 */
	public float getValue( ParameterType type ) {
		return parameters.get( type ).getValue();
	}
	
	/**
	 * Sets one of the parameters.
	 * 
	 * @param type
	 *            Type of parameter to set.
	 * @param parameter
	 *            NeuronParameter to set at that position.
	 */
	public void setParameter( ParameterType type, NeuronParam parameter ) {
		parameters.put( type, parameter );
	}
	
	/**
	 * Sets all weights to the specified array of values.
	 * 
	 * @param weights
	 *            float array of weight values.
	 */
	public void setAllWeightsFixed( float[] weights ) {
		for ( int i = 0; i < weights.length; i++ ) {
			setWeight( i, new NeuronParam.Fixed( weights[i] ) );
		}
	}
	
	public NeuronParam getParameter( ParameterType type ) {
		return parameters.get( type );
	}
	
	/**
	 * Sets a single weight to the specified parameter.
	 * 
	 * @param neuronIndex
	 *            Type of parameter to set.
	 * @param parameter
	 *            NeuronParameter to set at that position.
	 */
	public void setWeight( int neuronIndex, NeuronParam parameter ) {
		weights.set( neuronIndex, parameter );
	}
	
	/**
	 * Gets the connection strength to another neuron in the network.
	 * 
	 * @param neuronIndex
	 *            Index of neuron in network
	 * @return connection strength.
	 */
	public float getWeight( int neuronIndex ) {
		return weights.get( neuronIndex ).getValue();
	}
	
	public NeuronParam getWeightParameter( int neuronIndex ) {
		return weights.get( neuronIndex );
	}
	
	/**
	 * @return whether the tau, bias and gain parameters have been set.
	 */
	public boolean areAllParametersSet() {
		return parameters.containsKey( ParameterType.TAU ) &&
				parameters.containsKey( ParameterType.BIAS ) &&
				parameters.containsKey( ParameterType.GAIN );
	}
	
	@Override
	public String toString() {
		String tauS = parameters.containsKey( ParameterType.TAU ) ? parameters.get( ParameterType.TAU ).toString() : "not set";
		String biasS = parameters.containsKey( ParameterType.BIAS ) ? parameters.get( ParameterType.BIAS ).toString() : "not set";
		String gainS = parameters.containsKey( ParameterType.GAIN ) ? parameters.get( ParameterType.GAIN ).toString() : "not set";
		
		return String.format( "[Neuron, tau=%s bias=%s gain=%s nweights=%d]", tauS, biasS, gainS, weights.size() );
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Neuron))
			return false;
		
		Neuron other = (Neuron)obj;
		
		for(ParameterType type : ParameterType.values()) {
			if( parameters.containsKey( type ) != other.parameters.containsKey( type ) )
				return false;
			if( parameters.containsKey( type ) && !parameters.get( type ).equals( other.parameters.get( type ) ))
				return false;				
		}
		
		if(weights.size() != other.weights.size())
			return false;
		for(int i = 0; i< weights.size(); i++) {
			if(!weights.get(i).equals(other.weights.get(i)))
				return false;
		}

		return true;
	}
	
}
