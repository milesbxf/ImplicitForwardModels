package mb.projectmain.experiment.inputs;

import mb.util.MathUtils;

/**
 * Provides square/pulse shaped input.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class SquareWaveInputProvider extends SinusoidalInputProvider {
	
	/**
	 * @param freqM
	 *            Global frequency modulator.
	 * @param ampM
	 *            Global amplitude modulator.
	 */
	public SquareWaveInputProvider ( float freqM, float ampM ) {
		super( freqM, ampM );
	}
	
	/**
	 * Creates a SquareWaveInputProvider with the default parameters of freqM =
	 * 0.25, ampM = 2
	 */
	public SquareWaveInputProvider () {
		super( 0.25f, 2 );
	}
	
	@Override
	public int getNumParams() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public float getInput( float x ) {
		return ampM * p[0] * Math.signum( MathUtils.sinf( freqM * p[1] * x ) );
	}
	
}
