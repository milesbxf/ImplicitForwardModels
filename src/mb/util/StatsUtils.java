package mb.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains some basic statistics utility functions, e.g. getting the mean and
 * variance of an array
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class StatsUtils {
	
	/**
	 * From a list of float arrays, calculates the mean at each index and
	 * returns an array of means.
	 * 
	 * @param listOfArrays
	 *            List with float arrays. All arrays must have the same length.
	 * @return A float array with the mean at each index of the given arrays.
	 */
	public static float[] calculateMeansOfArrays( List<float[]> listOfArrays ) {
		
		int lengthPerArray = listOfArrays.get( 0 ).length;
		
		float[] meanTS = new float[lengthPerArray];
		
		// loop through arrays and sum each index
		for ( int i = 0; i < listOfArrays.size(); i++ ) {
			float[] outputs = listOfArrays.get( i );
			
			for ( int t = 0; t < lengthPerArray; t++ ) {
				meanTS[t] += outputs[t];
			}
		}
		
		// loop through arrays and divide by size to get mean
		for ( int t = 0; t < lengthPerArray; t++ ) {
			meanTS[t] /= listOfArrays.size();
		}
		
		return meanTS;
	}
	
	/**
	 * Calculates the variance of a list of arrays, using a given mean array,
	 * according to the formula
	 * 
	 * @param listOfArrays
	 *            List with float arrays. All arrays must have the same length.
	 * @param meanTS
	 *            Array of means for each index. This will be used to calculate
	 *            variance.
	 * 
	 * @return a List of float arrays containing the variance of each float
	 *         array passed to the function
	 */
	public static List<float[]> calculateVarianceWithMeans( List<float[]> listOfArrays, float[] meanTS ) {
		
		int timeSteps = listOfArrays.get( 0 ).length;
		
		List<float[]> variances = new ArrayList<>( listOfArrays.size() );
		
		for ( int i = 0; i < listOfArrays.size(); i++ ) {
			
			float[] var = new float[timeSteps];
			float[] output = listOfArrays.get( i );
			
			for ( int t = 0; t < timeSteps; t++ ) {
				float diff = (meanTS[t] - output[t]);
				var[t] = diff * diff;
			}
			variances.add( var );
		}
		
		return variances;
	}
	
	/**
	 * Calculates the mean of the given array
	 * 
	 * @param array
	 *            Array to calculate mean of.
	 * @return Float mean value of array.
	 */
	public static float calculateMean( float[] array ) {
		
		float sum = 0;
		for ( float n : array ) {
			sum += n;
		}
		return sum / array.length;
	}
}
