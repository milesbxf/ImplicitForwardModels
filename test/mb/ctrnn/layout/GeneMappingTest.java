package mb.ctrnn.layout;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneMappingTest {

	@Test
	public void twoGeneMappingsAreEqual() {
		GeneMapping mapping1 = new GeneMapping();
		mapping1.setNewGeneMapping(0, new NeuronParam.Gene(new Range(0,10)));
		mapping1.setNewGeneMapping(1, new NeuronParam.Gene(new Range(0,30)));
		
		GeneMapping mapping2 = new GeneMapping();
		mapping2.setNewGeneMapping(0, new NeuronParam.Gene(new Range(0,10)));
		mapping2.setNewGeneMapping(1, new NeuronParam.Gene(new Range(0,30)));
		
		assertTrue(mapping1.equals(mapping2));
	}


	@Test
	public void twoGeneMappingsAreNotEqual() {
		GeneMapping mapping1 = new GeneMapping();
		mapping1.setNewGeneMapping(0, new NeuronParam.Gene(new Range(0,10)));
		mapping1.setNewGeneMapping(1, new NeuronParam.Gene(new Range(0,30)));
		
		GeneMapping mapping2 = new GeneMapping();
		mapping2.setNewGeneMapping(0, new NeuronParam.Gene(new Range(0,10)));
		mapping2.setNewGeneMapping(2, new NeuronParam.Gene(new Range(0,30)));
		
		assertFalse(mapping1.equals(mapping2));
	}
	
}
