package mb.evolution;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

public class PopulationStatsTest {

	PopulationStats stats;
	List<Individual> testInds;
	@Mocked Genotype geno;
	
	@Before
	public void setUp() {
		stats = new PopulationStats();
		testInds = new ArrayList<>();
		for (int i = 1; i < 6; i++) {
			Individual ind = Individual.withRandomGenome(5);			
			ind.setFitness(i);			
			testInds.add(ind);
		}
	}
	
	@Test
	public void nGenIncreasesByOneOnUpdate() {
		assertEquals(0,stats.getNumberGenerations());
		stats.update(testInds);
		assertEquals(1,stats.getNumberGenerations());
	}
	
	@Test
	public void maxFitChoosesLargestFitness() throws Exception {
		stats.update(testInds);
		float exp = 5,
				result = stats.getLastMaxFit();
		assertEquals(exp,result,0.00001f);
	}

	@Test
	public void minFitChoosesSmallestFitness() throws Exception {
		stats.update(testInds);
		float exp = 1,
				result = stats.getLastMinFit();
		assertEquals(exp,result,0.00001f);		
	}
	
	@Test
	public void avgFitisAverageFitness() throws Exception {
		stats.update(testInds);
		float exp = 3,
				result = stats.getLastAvgFit();
		assertEquals(exp,result,0.00001f);		
	}
	
	@Test
	public void varIsVariance() throws Exception {
		stats.update(testInds);
		float exp = 10,
				result = stats.getLastVariance();
		assertEquals(exp,result,0.00001f);		
	}
	
	
}
