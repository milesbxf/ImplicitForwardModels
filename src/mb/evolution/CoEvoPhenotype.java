package mb.evolution;

import java.util.List;

/**
 * Represents a Phenotype, or a fitness function that can translate genes into a
 * fitness value
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public interface CoEvoPhenotype {
	
	/**
	 * Calculates the fitness of a particular array of genes, using the population of Individuals given somehow.
	 * @param individuals Population of individuals, typically from a competing population.
	 * @param genes Float array of gene values
	 * @return a scalar fitness value
	 */
	public float calculateFitness( List<Individual> individuals, float[] genes );
	
	/**
	 * @return the number of genes this phenotype accepts
	 */
	public int getGenotypeLength();
	
}
