package mb.projectmain.experiment.inputs;

import mb.util.MathUtils;

/**
 * Provides complex sinusoidal input by adding many sine waves together.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class SinusoidalInputProvider implements InputProvider {
	/**
	 * @param freqM
	 *            Frequency multiplier.
	 * @param ampM
	 *            Amplitude multiplier.
	 */
	public SinusoidalInputProvider ( float freqM, float ampM ) {
		this.freqM = freqM;
		this.ampM = ampM;
	}
	
	final float			freqM, ampM;
	protected float[]	p	= new float[6];
	
	/**
	 * Creates a SinusoidalInputProvider with the default parameters of freqM =
	 * 0.25, ampM = 2
	 */
	public SinusoidalInputProvider () {
		this( 0.25f, 2 );
	}
	
	@Override
	public void setParams( float[] params ) {
		this.p = params;
		
	}
	
	@Override
	public int getNumParams() {
		return 6;
	}
	
	/**
	 * calculates input by adding six sine waves with the parameters.
	 */
	@Override
	public float getInput( float x ) {
		return ampM * (p[0] * MathUtils.sinf( freqM * p[1] * x ) + p[2] * MathUtils.sinf( freqM * p[3] * x ) + p[4] * MathUtils.sinf( freqM * p[5] * x ));
	}
}