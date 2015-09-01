package mb.projectmain.experiment.inputs;

/**
 * Defines the interface by which all the input types follow so that they can be
 * used interchangeably. Basically a small function object that may be supplied
 * with parameters (e.g. gene values) and returns an input value based on the
 * given x value.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public interface InputProvider {
	/**
	 * Sets parameters (e.g. from genes) that modulate this InputProvider. Array
	 * must be same length as the result of getNumParams().
	 * 
	 * @param params
	 *            Parameters to set.
	 */
	public void setParams( float[] params );
	
	/**
	 * @return the number of parameters this InputProvider accepts.
	 */
	public int getNumParams();
	
	/**
	 * @param x
	 *            value to modulate InputProvider with, e.g. time step
	 * @return the result of the underlying function of this InputProvider.
	 */
	public float getInput( float x );
}