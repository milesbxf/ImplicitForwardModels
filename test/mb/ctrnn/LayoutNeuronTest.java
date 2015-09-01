package mb.ctrnn;

import static org.junit.Assert.*;
import mb.ctrnn.layout.Neuron;
import mb.ctrnn.layout.NeuronParam;
import mb.ctrnn.layout.Range;
import mb.ctrnn.layout.Neuron.ParameterType;

import org.junit.Test;

public class LayoutNeuronTest {

	@Test
	public void geneParameterMapsGeneCorrectly() {
		NeuronParam.Gene param = new NeuronParam.Gene(new Range(0,1));
		
		param.setGeneValue(1);		
		float expected = 1, actual = param.getValue();
		
		assertEquals(expected,actual,0.00001f);
		
		param.setGeneValue(-1);		
		expected = 0; actual = param.getValue();
		
		assertEquals(expected,actual,0.00001f);

		param.setGeneValue(0);		
		expected = 0.5f; actual = param.getValue();
		
		assertEquals(expected,actual,0.00001f);	
	}
	
	@Test
	public void addingParameterRetrievesValue() throws Exception {
		Neuron neuron = new Neuron(2);
		neuron.setParameter(ParameterType.TAU, new NeuronParam.Fixed(1f));
		
		float expected = 1f, actual = neuron.getValue(ParameterType.TAU);

		assertEquals(expected,actual,0.0000001f);
	}
	
	@Test
	public void unconnectedNeuronHasZeroWeight() throws Exception {
		Neuron neuron = new Neuron(2);
		
		float expected = 0f, actual = neuron.getWeight(0);
		assertEquals(expected,actual,0.0000001f);
	}
	
	@Test
	public void settingWeightReturnsThatValueWhenRequested() throws Exception {
		Neuron neuron = new Neuron(2);
		neuron.setWeight(0, new NeuronParam.Fixed(1f));
		
		float expected = 1f, actual = neuron.getWeight(0);
		assertEquals(expected,actual,0.0000001f);
	}
	
}
