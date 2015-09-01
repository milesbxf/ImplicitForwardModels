package mb.util;

/**
 * Contains mathematical utility functions.
 * 
 * @author Miles Bryant
 *
 */
public class MathUtils {
	
	/**
	 * Standard sigmoid function (1/(1+e^-x)). If x is NaN, result is NaN. If x
	 * is +infinity, result = 0. If x is zero, result = 0.5. If x is -infinity,
	 * result = 1.
	 * 
	 * @param x number to get sigmoid of
	 * @return float sigmoid of x.
	 */
	public static float sigmoid( float x ) {
		float result = 1.0f / (1 + (float) Math.exp( -x ));
		if ( Float.isNaN( result ) ) {
			throw new AssertionError();
		}
		return result;
	}
	
	/**
	 * Convenience method for getting floating point value of Math.sin(x).
	 * @param x number to get sine of
	 * @return float of Math.sin(x)
	 */
	public static float sinf( float x ) {
		return (float) Math.sin( x );
	}
	
	/**
	 * Convenience method for getting floating point value of Math.floor(x).
	 * @param x number to get floor of
	 * @return float of Math.floor(x)
	 */
	public static float floorF( float x ) {
		return (float) Math.floor( x );
	}
	
}
