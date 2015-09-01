package mb.evolution;

import java.util.Arrays;
import java.util.Random;

/**
 * Maintains an array of genes and allows genotype level operations on them
 * (such as mutation and crossover).
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class Genotype {
	
	private final static Random	rand	= new Random();
	
	/**
	 * Static factory method producing a random genotype with values in the
	 * range [-1,1].
	 * 
	 * @param numberOfGenes
	 *            Number of genes to create.
	 * @return a Genotype with a random array of genes.
	 */
	public static Genotype withRandomGenome( int numberOfGenes ) {
		float[] initGenes = new float[numberOfGenes];
		
		for ( int i = 0; i < numberOfGenes; i++ ) {
			initGenes[i] = rand.nextFloat() * 2f - 1f;
		}
		
		return new Genotype( initGenes );
	}
	
	/**
	 * Static factory method producing a new Genotype with the genes specified.
	 * 
	 * @param genes
	 *            Genes to create this Genotype with.
	 * @return a Genotype with a specified genes.
	 */
	public static Genotype withGenes( float[] genes ) {
		return new Genotype( genes );
	}
	
	private final float[]	genes;
	
	private Genotype ( float[] genes ) {
		this.genes = genes;
	}
	
	/**
	 * @return the float array of genes
	 */
	public float[] getGenes() {
		return genes;
	}
	
	/**
	 * Adds creep (Gaussian noise) to each of these genes, mutating them
	 * slightly.
	 * 
	 * @param mutationSD
	 *            Standard deviation of the noise.
	 * @return a new Genotype with mutated genes.
	 */
	public Genotype mutate( float mutationSD ) {
		float[] newGenome = Arrays.copyOf( genes, genes.length );
		for ( int i = 0; i < genes.length; i++ ) {
			newGenome[i] = mutateGene( newGenome[i], mutationSD );
		}
		return new Genotype( newGenome );
	}
	
	private float mutateGene( float origGene, float mutationSD ) {
		float newGene = (float) (origGene + mutationSD * rand.nextGaussian());
		
		// keep genes within bounds
		
		while ( 1 < newGene )
			newGene -= 1;
		while ( newGene < -1 )
			newGene += 1;
		
		return newGene;
	}
	
	/**
	 * Copies genes from another genotype with probability p_crossover.
	 * 
	 * @param genotype2
	 *            Other genotype to copy from.
	 * @param p_crossover
	 *            Probability of crossover, in range [0,1].
	 * @return a new Genotype with crossed over genes.
	 */
	public Genotype crossover( Genotype genotype2, float p_crossover ) {
		float[] newGenome = Arrays.copyOf( genes, genes.length ), secondGenotype = genotype2.getGenes();
		for ( int i = 0; i < genes.length; i++ ) {
			if ( rand.nextFloat() < p_crossover ) {
				newGenome[i] = secondGenotype[i];
			}
		}
		return new Genotype( newGenome );
	}
	
	@Override
	public String toString() {
		return "Genotype n=" + genes.length + " genes=" + Arrays.toString( getGenes() ) + "]";
	}
	
}
