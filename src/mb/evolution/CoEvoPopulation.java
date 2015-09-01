package mb.evolution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import mb.datalog.CSVWriter;
import mb.datalog.CSVWriter.CSVParamBuilder;

/**
 * The CoEvoPopulation maintains an Input and Model population. By calling their
 * respective generation methods, generations can be manually controlled. This
 * also performs all the reproduction, e.g. crossover and mutation.
 * 
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class CoEvoPopulation {
	
	Logger	LOG	= Logger.getLogger( CoEvoPopulation.class.getName() );
	
	/**
	 * Creates a new CoEvoPopulation with the specified number of individuals in
	 * each population, and calculates initial fitness for all individuals.
	 * 
	 * @param inputPheno
	 *            CoEvoPhenotype for the input population.
	 * @param modelPheno
	 *            CoEvoPhenotype for the model population.
	 * @param nPopulation
	 *            Number of individuals in each population.
	 * @param mutSD
	 *            Standard deviation for Gaussian noise added to genes
	 *            (creep/mutation).
	 * @param pCross
	 *            Probability of crossover from winner's genes to loser's in
	 *            reproduction events, in range [0,1].
	 * @param demeSize
	 *            Geographical limitation meaning individuals can only reproduce
	 *            with those within a certain index.
	 */
	public CoEvoPopulation ( CoEvoPhenotype inputPheno, CoEvoPhenotype modelPheno, int nPopulation, float mutSD, float pCross, int demeSize ) {
		LOG
				.info( String
						.format(
									"Initialising population using %s and %s phenotypes with %d individuals, deme size %d, mutation SD of %f and crossover probability %f\n",
									inputPheno.getClass().getName(), modelPheno.getClass().getName(), nPopulation, demeSize, mutSD, pCross ) );
		
		LOG.setUseParentHandlers( true );
		
		this.inputPheno = inputPheno;
		this.modelPheno = modelPheno;
		this.mutSD = mutSD;
		this.pCross = pCross;
		this.demeSize = demeSize;
		this.nPopulation = nPopulation;
		
		inputInds = new ArrayList<>( nPopulation );
		modelInds = new ArrayList<>( nPopulation );
		
		stats1 = new PopulationStats();
		stats2 = new PopulationStats();
		
		LOG.info( "Creating genotypes and calculating fitnesses..." );
		
		// generate both populations first, then calculate fitnesses
		
		for ( int i = 0; i < nPopulation; i++ ) {
			Individual inputI = Individual.withRandomGenome( inputPheno.getGenotypeLength() );
			inputInds.add( inputI );
			
			Individual modelI = Individual.withRandomGenome( modelPheno.getGenotypeLength() );
			modelInds.add( modelI );
			
			LOG.info( String.format( "Created %d of %d genotypes. G1=%s\tG2=%s", i, nPopulation, inputI.getGenotype().toString(), modelI
					.getGenotype().toString() ) );
		}
		
		for ( int i = 0; i < nPopulation; i++ ) {
			float fInput = inputPheno.calculateFitness( modelInds, inputInds.get( i ).getGenotype().getGenes() );
			float fModel = modelPheno.calculateFitness( inputInds, modelInds.get( i ).getGenotype().getGenes() );
			
			inputInds.get( i ).setFitness( fInput );
			modelInds.get( i ).setFitness( fModel );
			LOG.info( String.format( "Done %d of %d individuals, fit1 = %f, fit2 = %f", i, nPopulation, fInput, fModel ) );
			
		}
		
		LOG.info( "Finished calculating fitnesses." );
		
	}
	
	/**
	 * @return the index of an individual within the population bounds
	 */
	public int selectIndividual() {
		return rand.nextInt( nPopulation );
	}
	
	/**
	 * Selects an individual within the allowed deme bounds. Wraps so that if
	 * the proposed index was too high, it restarts
	 * 
	 * @param firstIndex
	 *            Index of first individual selected
	 * @param demeSize
	 *            Size of limiting region
	 * @return index of individual within first individual's bounds
	 */
	public int selectSecondIndividual( int firstIndex, int demeSize ) {
		int newIndex = firstIndex;
		while ( newIndex == firstIndex ) {
			newIndex = firstIndex + rand.nextInt( demeSize );
		}
		while ( nPopulation - 1 < newIndex ) {
			newIndex -= nPopulation;
		}
		return newIndex;
	}
	
	/**
	 * Performs selection and updates stats for the input generation.
	 */
	public void doInputGeneration() {
		for ( int i = 0; i < nPopulation; i++ ) {
			doSelection( inputInds, modelInds, inputPheno );
		}
		stats1.update( inputInds );
	}
	
	/**
	 * Performs selection and updates stats for the model generation.
	 */
	public void doModelGeneration() {
		for ( int i = 0; i < nPopulation; i++ ) {
			doSelection( modelInds, inputInds, modelPheno );
		}
		stats2.update( modelInds );
	}
	
	/**
	 * Performs a selection event on the specified population and phenotype.
	 * 
	 * @param primaryInd
	 *            Population to select from.
	 * @param secondaryInd
	 *            Population to pass to the phenotype.
	 * @param pheno
	 *            Phenotype to calculate fitness with.
	 */
	protected void doSelection( List<Individual> primaryInd, List<Individual> secondaryInd, CoEvoPhenotype pheno ) {
		int index1 = selectIndividual(), index2 = selectSecondIndividual( index1, demeSize );
		
		float fitness1 = pheno.calculateFitness( secondaryInd, primaryInd.get( index1 ).getGenotype().getGenes() );
		primaryInd.get( index1 ).setFitness( fitness1 );
		float fitness2 = pheno.calculateFitness( secondaryInd, primaryInd.get( index2 ).getGenotype().getGenes() );
		primaryInd.get( index2 ).setFitness( fitness2 );
		int loser = doReproductionEvent( primaryInd, index1, index2, fitness1, fitness2 );
		float loserFitness = pheno.calculateFitness( secondaryInd, primaryInd.get( loser ).getGenotype().getGenes() );
		primaryInd.get( loser ).setFitness( loserFitness );
		
	}
	
	/**
	 * Performs a reproduction event. If fitness(1) > fitness(2) individual 1 is
	 * the winner; and these values are passed to reproduce()
	 * 
	 * @param primaryInd
	 *            Population to draw individuals from
	 * @param index1
	 *            First index chosen
	 * @param index2
	 *            Second index chosen
	 * @param fitness1
	 *            Fitness associated with first index
	 * @param fitness2
	 *            Fitness associated with second index
	 * @return index of losing individual
	 */
	protected int doReproductionEvent( List<Individual> primaryInd, int index1, int index2, float fitness1, float fitness2 ) {
		
		int winningIndex = 0, losingIndex = 0;
		if ( fitness1 > fitness2 ) {
			winningIndex = index1;
			losingIndex = index2;
		} else if ( fitness1 < fitness2 ) {
			winningIndex = index2;
			losingIndex = index1;
		} else {
			// both fitnesses equal, probable that both parameter sets are same
			// but either way we'll randomly pick a winner
			
			if ( rand.nextBoolean() ) {
				winningIndex = index1;
				losingIndex = index2;
			} else {
				winningIndex = index2;
				losingIndex = index1;
			}
		}
		return reproduce( primaryInd, losingIndex, winningIndex );
	}
	
	/**
	 * Performs crossover and mutation on a losing genotype.
	 * 
	 * @param primaryInd
	 *            Population to draw individuals from
	 * @param losingIndex
	 *            Index of losing individual
	 * @param winningIndex
	 *            Index of winning individual
	 * @return index of losing individual
	 */
	public int reproduce( List<Individual> primaryInd, int losingIndex, int winningIndex ) {
		// crossover and mutation produce a new genotype
		
		Genotype newGeno = primaryInd.get( losingIndex ).getGenotype()
				.crossover( primaryInd.get( winningIndex ).getGenotype(), pCross )
				.mutate( mutSD );
		// check for out of range genes
		for ( float gene : newGeno.getGenes() ) {
			if ( gene < -1 || gene > 1 ) {
				LOG.warning( "Gene value is out of range [-1,1]: " + gene );
			}
		}
		
		primaryInd.get( losingIndex ).setGenotype( newGeno );
		return losingIndex;
	}
	
	/**
	 * Saves gene values and fitnesses to CSV files.
	 * 
	 * @param file1
	 *            File for input population.
	 * @param file2
	 *            File for model population.
	 * @throws IOException
	 *             if there is an error with file handling
	 */
	public void savePopToCSV( File file1, File file2 ) throws IOException {
		saveIndToCSV( file1, inputPheno, inputInds );
		saveIndToCSV( file2, modelPheno, modelInds );
	}
	
	/**
	 * Saves an individual population to a CSV file.
	 * 
	 * @param file
	 * @param phenotype
	 * @param individuals
	 * @throws IOException
	 */
	private void saveIndToCSV( File file, CoEvoPhenotype phenotype, List<Individual> individuals ) throws IOException {
		LOG.info( "Saving population genotypes to file '" + file.getAbsolutePath() + "'" );
		CSVParamBuilder cpb = new CSVParamBuilder();
		cpb.addHeader( "fitness" );
		
		for ( int i = 0; i < phenotype.getGenotypeLength(); i++ ) {
			cpb.addHeader( "g" + i );
		}
		
		for ( Individual ind : individuals ) {
			List<Object> row = new ArrayList<>();
			
			row.add( ind.getFitness() );
			for ( Float gene : ind.getGenotype().getGenes() ) {
				row.add( gene );
			}
			cpb.addDataRow( row );
		}
		
		CSVWriter.writeToCSVFile( file, cpb );
		
	}
	
	/**
	 * Lists of individuals.
	 */
	protected final List<Individual>	inputInds, modelInds;
	
	private final CoEvoPhenotype		inputPheno, modelPheno;
	
	protected final PopulationStats		stats1, stats2;
	protected final float				mutSD;
	protected final float				pCross;
	protected final int					demeSize, nPopulation;
	private static final Random			rand	= new Random();
	
	public PopulationStats getInputStats() {
		return stats1;
	}
	
	public PopulationStats getModelStats() {
		return stats2;
	}
	
}
