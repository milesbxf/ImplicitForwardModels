package mb.ctrnn.layout;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import mb.ctrnn.layout.NeuronParam.Fixed;
import mb.ctrnn.layout.NeuronParam.Gene;

import org.junit.Test;

public class NeuronParamTest {

	@Test
	public void testGenesEqual() {
		Gene gene1 = new Gene(new Range(0,10));
		Gene gene2 = new Gene(new Range(0,10));
		
		assertTrue(gene1.equals(gene2));
	}
	
	@Test
	public void testGenesNotEqual() {
		Gene gene1 = new Gene(new Range(0,10));
		Gene gene2 = new Gene(new Range(0,20));
		
		assertFalse(gene1.equals(gene2));
	}

	@Test
	public void testFixedEqual() throws Exception {
		Fixed fixed1 = new Fixed(10);
		Fixed fixed2 = new Fixed(10);
		assertTrue(fixed1.equals(fixed2));
	}
	

	@Test
	public void testFixedNotEqual() throws Exception {
		Fixed fixed1 = new Fixed(10);
		Fixed fixed2 = new Fixed(5);
		assertFalse(fixed1.equals(fixed2));
	}
	
}
