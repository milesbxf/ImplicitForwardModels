package mb.ctrnn;

import static org.junit.Assert.*;
import mb.ctrnn.layout.GeneMapping;
import mb.ctrnn.layout.NeuronParam;
import mb.ctrnn.layout.Range;

import org.junit.Test;

public class GeneMappingTest {

	@Test
	public void settingGeneLocationAddsParameterToThatLocation() {
		GeneMapping mapping = new GeneMapping();
		
		NeuronParam.Gene gene = new NeuronParam.Gene(new Range(0,1));
		gene.setGeneValue(1f);
		
		mapping.setNewGeneMapping(3,gene);
		
		float expected = 1f,actual = mapping.getValueAtGene(3);
		
		assertEquals(expected,actual, 0.00001f);
	}

}
