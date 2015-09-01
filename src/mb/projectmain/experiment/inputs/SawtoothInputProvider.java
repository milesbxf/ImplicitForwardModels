package mb.projectmain.experiment.inputs;

import mb.util.MathUtils;


/**
 * Provides sawtooth shaped input
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class SawtoothInputProvider extends SinusoidalInputProvider {
	
	public SawtoothInputProvider () {
		super(100f,2f);
	}

	public SawtoothInputProvider ( float freqM, float ampM) {
		super(freqM,ampM);
	}
	
	
	@Override
	public int getNumParams() {
		// TODO Auto-generated method stub
		return 2;
	}
	
	@Override
	public float getInput( float x ) {
		float a = x / (freqM * p[1]);
		return ampM * p[0] * 2 * (a - MathUtils.floorF(0.5f+a));
	}
	
	
}
