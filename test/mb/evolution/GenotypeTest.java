package mb.evolution;

import static org.junit.Assert.*;

import java.util.Random;

import mockit.Capturing;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Test;

public class GenotypeTest {
	
	@Mocked Random rand;
	final float[] expectedGenome = new float[] { -1f,-0.8f,-0.6f,-0.4f,-0.2f,0f,0.2f,0.4f,0.6f,0.8f};

	@Test
	public void withNewRandomGenomeCreatesRandomGenotype() {
		
		new Expectations() {{
			rand.nextFloat();
			result = expectedGenome[0];
			result = expectedGenome[1];
			result = expectedGenome[2];
			result = expectedGenome[3];
			result = expectedGenome[4];
			result = expectedGenome[5];
			result = expectedGenome[6];
			result = expectedGenome[7];
			result = expectedGenome[8];
			result = expectedGenome[9];
		}};
		
		Genotype geno = Genotype.withRandomGenome(10);
		
		assertArrayEquals(expectedGenome, geno.getGenes(),0.00001f);
	}
	
	@Test
	public void stdDevAltersMutationCreep() throws Exception {
		final float[] initGenome = new float[]{0f,0f,0f,0f,0f};
		final float[] muts = new float[]{0.1f,0.2f,0.3f,0.4f,0.5f};	

		Genotype genoBefore = Genotype.withGenes(initGenome);

		new Expectations() {{
			rand.nextGaussian();
			result = muts[0];
			result = muts[1];
			result = muts[2];
			result = muts[3];
			result = muts[4];
		}};
		

		float[] expected = new float[]{0.2f,0.4f,0.6f,0.8f,1f};
		float[] result = genoBefore.mutate(2f).getGenes();

		assertArrayEquals(expected, result,0.00001f);
	}
	
	@Test
	public void mutateAltersAllGenes() throws Exception {
		final float[] initGenome = new float[]{0f,0f,0f,0f,0f};
		final float[] muts = new float[]{0.1f,0.2f,0.3f,0.4f,0.5f};		
		Genotype genoBefore = Genotype.withGenes(initGenome);

		new Expectations() {{
			rand.nextGaussian();
			result = muts[0];
			result = muts[1];
			result = muts[2];
			result = muts[3];
			result = muts[4];
		}};
		
		
		float[] result = genoBefore.mutate(1f).getGenes();
		
		assertArrayEquals(muts, result,0.00001f);
	}
	
	@Test
	public void mutatedValuesOutOfRangeAreReflected() throws Exception {
		final float[] initGenome = new float[]{0f,0f,0f,0f};
		final float[] muts = new float[]{1.5f,-1.5f,1.8f,-1.8f};

		Genotype genoBefore = Genotype.withGenes(initGenome);
		
		new Expectations() {{
			rand.nextGaussian();
			result = muts[0];
			result = muts[1];
			result = muts[2];
			result = muts[3];
		}};
		
		float[] expected = new float[]{0.5f,-0.5f,0.8f,-0.8f};
		float[] result = genoBefore.mutate(1f).getGenes();

		assertArrayEquals(expected, result,0.00001f);		
	}
	
	@Test
	public void crossOverWithAnotherGenomeRandomlyCopiesGenes() throws Exception {
		Genotype geno1 = Genotype.withGenes(new float[]{0f,0f,0f,0f}),
				geno2 = Genotype.withGenes(new float[]{1f,1f,1f,1f});
		
		new Expectations() {{
			rand.nextFloat();
			result = 0.4f; result = 0.6f; result = 0.4f; result = 0.6f;
		}};
				
		Genotype result = geno1.crossover(geno2,0.5f);
		
		float[] expected = new float[]{1f,0f,1f,0f};
		
		assertArrayEquals(expected, result.getGenes(),0.00001f);				
	}

}
