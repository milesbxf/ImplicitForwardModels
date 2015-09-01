package mb.datalog;

import java.util.List;

/**
 * A mixin for a class thats data can be logged. Each timestep, the getData()
 * method is called to provide a list of Objects.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public interface Loggable {
	
	/**
	 * Called by the corresponding DataLogger when its appendData() method is
	 * invoked. Should return data describing the object's current state, e.g.
	 * outputs and inputs of all neurons in a CTRNN at the current time step.
	 * 
	 * @return
	 */
	public List<Object> getData();
	
	/**
	 * @return a list of column headers describing this Loggable's data.
	 */
	public List<String> getHeaders();
}
