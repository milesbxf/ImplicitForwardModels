package mb.evolution;

/**
 * A simple data structure for storing genotypes and caching fitness values.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class Individual {
	private float		fitness;
	private Genotype	genotype;
	
	/**
	 * Creates a new Individual with a random Genotype with the specified number
	 * of genes.
	 * 
	 * @param nGenes
	 *            Number of genes for the new individual.
	 * @return
	 */
	public static Individual withRandomGenome( int nGenes ) {
		Individual ind = new Individual();
		ind.setGenotype( Genotype.withRandomGenome( nGenes ) );
		return ind;
	}
	
	/**
	 * @return the currently set fitness (does not calculate it).
	 */
	public float getFitness() {
		return fitness;
	}
	
	/**
	 * Sets a fitness value (e.g. calculated elsewhere). Purely for stats, not
	 * used by the population for reproduction.
	 * 
	 * @param fitness
	 */
	public void setFitness( float fitness ) {
		this.fitness = fitness;
	}
	
	/**
	 * @return the Genotype of this Individual.
	 */
	public Genotype getGenotype() {
		return genotype;
	}
	
	/**
	 * Associates a new Genotype with this Individual.
	 * @param genotype
	 */
	public void setGenotype( Genotype genotype ) {
		this.genotype = genotype;
	}
	
}
