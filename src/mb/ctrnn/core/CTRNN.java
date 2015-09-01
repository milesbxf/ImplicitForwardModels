package mb.ctrnn.core;

/**
 * Simulates a continuous time recurrent network of neurons, with tau,bias and
 * gain parameters and connection weights.
 * 
 * @author Miles Bryant
 */
public class CTRNN {

	protected final int n; // number of neurons
	/**
	 * parameters
	 */
	protected final float[] taus, biases, gains;
	private float[] states, outputs; //states is an intermediary
	protected final float[][] weights; //connection strengths between neurons, 0 if not connected
	
	/**
	 * Initialises a new CTRNN with the specified parameters.
	 * @param n Number of neurons.
	 * @param taus Array of tau parameters; determines time constant for each neuron.
	 * @param biases Array of bias parameters; determines biasing for each neuron.
	 * @param gains Array of gain parameters; determines neural gain for each neuron.
	 * @param weights Matrix of weight connections; weights[i][j] determines connection 
	 * strength between neuron i and neuron j. No connection is indicated by 0.
	 */
	public CTRNN(int n, float[] taus, float[] biases, float[] gains, float[][] weights) {
		
		this.n = n;
		validate(taus, "taus");
		this.taus = taus;
		validate(biases, "biases");
		this.biases = biases;
		validate(gains, "gains");
		this.gains = gains;
		if(weights.length != n)
			throw new IllegalArgumentException(String.format("Weights should be of length %d; got an array of length %d",n,weights.length));
		this.weights = weights;
		
		this.states = new float[n];
		this.outputs = new float[n];
		
		//initialise states to 0.5 and outputs to 0
		
		for (int i = 0; i < n; i++) {
			states[i] = 0.5f; outputs[i] = 0f;
		}
	}
	
	private void validate(float[] array,String arrayName) {
		//checks that the array pass is of correct length
		if(array.length != n)
			throw new IllegalArgumentException(String.format("%s should be of length %d; got an array of length %d",arrayName,n,array.length));
	}
		
	/**
	 * Integrates one step using Euler's method, updating neural outputs.
	 * Adapted from Randall Beer's C code available at http://mypage.iu.edu/~rdbeer/.
	 * 
	 * @param stepSize Timestep value for integration.
	 * @param inputs Array of values to input to each neuron; sensory neurons will typically have nonzero inputs.
	 */
	public void step(float stepSize, float[] inputs) {
		for (int i = 0; i < n; i++) {
			float input;
			input = inputs[i];
			for (int j = 0; j < n; j++)
				input += weights[j][i] * outputs[j];
			
			if(Float.isNaN( states[i] )) {
				throw new AssertionError();
			}
			
			states[i] += stepSize * (1/taus[i]) * (input - states[i]);
			
			//switch activation function through commenting the appropriate line
			
//			outputs[i] = MathUtils.sigmoid(gains[i] * (states[i] + biases[i]));
			outputs[i] = (float) Math.tanh(gains[i] * (states[i] + biases[i]));
			

			if(Float.isNaN( outputs[i] )) {
				throw new AssertionError();
			}
		}
	}

	/**
	 * @return an array of the neuron outputs.
	 */
	public float[] getOutputs() {
		return outputs.clone();
	}
	
	@Override
	public Object clone() {
		CTRNN ctrnn = new CTRNN(n, taus.clone(), biases.clone(), gains.clone(), weights.clone());
		ctrnn.states=states.clone();
		ctrnn.outputs=outputs.clone();
		return ctrnn;
	}
	
	@Override
	public String toString() {
		return String.format("[CTRNN n=%d]",n);
	}
}
