package mb.evolution;

import java.util.ArrayList;
import java.util.List;

import mb.datalog.Loggable;

import com.google.common.collect.Lists;

/**
 * Keeps track of per-generation population statistics (e.g. max,min,avg
 * fitness/variance). Provides access to statistics as a Loggable interface.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class PopulationStats implements Loggable {
	private int					nGen		= 0;
	private final List<Float>	maxFitnesses, minFitnesses, avgFitnesses,
								variances;
	private int					indexMax	= -1;
	
	/**
	 * Initialises a blank PopulationStats.
	 */
	public PopulationStats () {
		maxFitnesses = new ArrayList<>();
		minFitnesses = new ArrayList<>();
		avgFitnesses = new ArrayList<>();
		variances = new ArrayList<>();
	}
	
	/**
	 * @return the number of generations/updates the stats have had.
	 */
	public Object getNumberGenerations() {
		return nGen;
	}
	
	/**
	 * Updates this stats, e.g. after a generation, with the current list of
	 * individuals.
	 * 
	 * @param individuals
	 */
	public void update( List<Individual> individuals ) {
		nGen++;
		
		float sum = 0;
		int n = individuals.size();
		
		//NaN is a flag to indicate that it hasn't been set yet
		float currentMax = Float.NaN, currentMin = Float.NaN;
		
		for ( int i = 0; i < n; i++ ) {
			float fitness = individuals.get( i ).getFitness();
			if ( Float.isNaN( fitness ) ) {
				throw new AssertionError(); //shouldn't happen!
			}
			if ( Float.isNaN( currentMax ) || fitness > currentMax ) {
				currentMax = fitness;
				indexMax = i;
			}
			if ( Float.isNaN( currentMin ) || fitness < currentMin ) {
				currentMin = fitness;
			}
			sum += fitness;
		}
		
		maxFitnesses.add( currentMax );
		minFitnesses.add( currentMin );
		
		float mean = sum / n;
		avgFitnesses.add( mean );
		
		float sumVar = 0;
		for ( Individual ind : individuals ) {
			float meanDiff = ind.getFitness() - mean;
			sumVar += meanDiff * meanDiff;
		}
		variances.add( sumVar );
	}
	
	/**
	 * @return the last maximum fitness recorded
	 */
	public float getLastMaxFit() {
		return maxFitnesses.get( maxFitnesses.size() - 1 );
	}
	
	/**
	 * @return the last minimum fitness recorded
	 */
	public float getLastMinFit() {
		return minFitnesses.get( minFitnesses.size() - 1 );
	}
	
	/**
	 * @return the last average fitness calculated
	 */
	public float getLastAvgFit() {
		return avgFitnesses.get( avgFitnesses.size() - 1 );
	}
	
	/**
	 * @return the last variance calculated
	 */
	public float getLastVariance() {
		return variances.get( variances.size() - 1 );
	}
	
	public List<Float> getAvgFitnesses() {
		return avgFitnesses;
	}
	
	public List<Float> getMaxFitnesses() {
		return maxFitnesses;
	}
	
	public List<Float> getMinFitnesses() {
		return minFitnesses;
	}
	
	public List<Float> getVariances() {
		return variances;
	}
	
	/**
	 * @return index of the individual with the best fitness
	 */
	public int getIndexMax() {
		return indexMax;
	}
	
	/**
	 * @return headers for console output
	 */
	public String getConsoleHeaders() {
		return "-----------------------------------------------\n" +
				"| Gen\t| Max\t\t| Min\t\t| Avg\t\t| Var\t\t|\n" +
				"-----------------------------------------------";
	}
	
	/**
	 * @return a line of console output
	 */
	public String getConsoleOutputLine() {
		return String.format( "| %d\t| %f\t| %f\t| %f\t| %f\t|", getNumberGenerations(), getLastMaxFit(), getLastMinFit(), getLastAvgFit(), getLastVariance() );
	}
	
	@Override
	public List<Object> getData() {
		return Lists.newArrayList( getNumberGenerations(), getLastMaxFit(), getLastMinFit(), getLastAvgFit(), getLastVariance() );
	}
	
	@Override
	public List<String> getHeaders() {
		return Lists.newArrayList( "Generation", "Max fitness", "Min fitness", "Avg fitness", "Variance" );
	}
	
}
