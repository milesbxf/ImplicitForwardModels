package mb.ctrnn;

import static org.junit.Assert.*;
import mb.ctrnn.core.CTRNN;
import mb.ctrnn.layout.CTRNNLayout;
import mb.ctrnn.layout.Neuron;
import mb.ctrnn.layout.Neuron.ParameterType;
import mockit.Deencapsulation;

import org.junit.Test;

public class CTRNNLayoutTest {

	@Test
	public void constructingCTRNNLayoutCreatesCorrectCTRNN() {
		CTRNNLayout layout = new CTRNNLayout();
		
		Neuron neuron1 = Neuron.withFixedValues(1f, 1f, 1f, new float[]{1f,1f});
		System.out.println(neuron1);
		Neuron neuron2 = Neuron.withFixedValues(0.5f, 0.5f, 0.5f, new float[]{0.5f,0.5f});
		System.out.println(neuron2);
		
		layout.addNeuron(neuron1);
		layout.addNeuron(neuron2);
		
		CTRNN ctrnn = layout.createCTRNN();
		
		float[] expecteds = new float[]{1f,0.5f};
		
		float[] taus = Deencapsulation.getField(ctrnn, "taus");
		float[] biases = Deencapsulation.getField(ctrnn, "biases");
		float[] gains = Deencapsulation.getField(ctrnn, "gains");
		
		float[][] expectedWeights = new float[][]{{1f,1f},{0.5f,0.5f}};
		float[][] weights = Deencapsulation.getField(ctrnn, "weights");
		
		assertArrayEquals("taus were different to expected",expecteds, taus,0.00001f);
		assertArrayEquals("biases were different to expected",expecteds, biases,0.00001f);
		assertArrayEquals("gains were different to expected",expecteds, gains,0.00001f);
		assertArrayEquals("weights[1] were different to expected",expectedWeights[0], weights[0],0.00001f);
		assertArrayEquals("weights[2] were different to expected",expectedWeights[1], weights[1],0.00001f);
		
	}
	
}
