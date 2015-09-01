package mb.projectmain.experiment.inputs;

/**
 * Provides triangle shaped input
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class TriangleInputProvider extends SawtoothInputProvider {
	
	public TriangleInputProvider ( float freqM, float ampM ) {
		super( freqM, ampM );
	}
	
	public TriangleInputProvider () {
		super( 100f, 2 );
	}
	
	@Override
	public float getInput( float x ) {
		return Math.abs( super.getInput( x ) );
	}
}
