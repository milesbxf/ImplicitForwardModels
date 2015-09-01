package mb.projectmain.experiment.inputs;


/**
 * Input with a flat sine wave, y=amp*sin(freq*x)
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class FlatSineInputProvider implements InputProvider {
	/**
	 * Creates a new flat sine wave provider with the form y=amp*sin(freq*x)
	 * 
	 * @param freq frequency of sine wave
	 * @param amp amplitude of sine wave
	 */
	public FlatSineInputProvider ( float freq, float amp) {
		this.freq = freq;
		this.amp = amp;
	}

	final float	freq, amp;
	float[]		p	= new float[6];
	
	/**
	 * Initialises a flat sine wave with freq = 1, amp = 1
	 */
	public FlatSineInputProvider () {
		this(1f,1);
	}
	
	
	
	@Override
	public void setParams( float[] params ) {
		this.p = params;
		
	}
	
	@Override
	public int getNumParams() {
		return 6;
	}
	
	@Override
	public float getInput( float x ) {
		return amp * sinf(freq * x);
	}
	
	/**
	 * Utility method for a floating point sine method.
	 * 
	 * @param x value to get sine of
	 * @return floating point value of sine
	 */
	public static float sinf( float x ) {
		return (float) Math.sin( x );
	}
}