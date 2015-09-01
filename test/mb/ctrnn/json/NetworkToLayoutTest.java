package mb.ctrnn.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.ctrnn.json.Network.ParamRange;
import mb.ctrnn.layout.CTRNNLayout;
import mb.ctrnn.layout.GeneMapping;
import mb.ctrnn.layout.Neuron;
import mb.ctrnn.layout.Neuron.ParameterType;
import mb.ctrnn.layout.NeuronParam;
import mb.ctrnn.layout.NeuronParam.Fixed;
import mb.ctrnn.layout.NeuronParam.Gene;
import mb.ctrnn.layout.Range;
import mockit.Deencapsulation;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class NetworkToLayoutTest {
	
	Network		network;
	CTRNNLayout	layout;
	
	@Before
	public void setUp() throws JSONNetworkParseError {
		network = JSONTestUtils.createNetwork();
		layout = network.getLayout();
	}
	
	@Test
	public void creates2Neurons() {
		assertEquals( 4, layout.getNumberOfNeurons() );
	}
	
	@Test
	public void createsFixedTau() throws Exception {
		Neuron neuron = layout.getNeuron( 1 );
		NeuronParam tau = neuron.getParameter( ParameterType.TAU );
		
		assertThat( tau, instanceOf( NeuronParam.Fixed.class ) );
		assertThat( ((Fixed) tau).getValue(), is( 3f ) );
	}
	
	@Test
	public void createsFixedBias() throws Exception {
		Neuron neuron = layout.getNeuron( 1 );
		NeuronParam bias = neuron.getParameter( ParameterType.BIAS );
		
		assertThat( bias, instanceOf( NeuronParam.Fixed.class ) );
		assertThat( ((Fixed) bias).getValue(), is( 4f ) );
	}
	
	@Test
	public void createsFixedGain() throws Exception {
		Neuron neuron = layout.getNeuron( 0 );
		NeuronParam gain = neuron.getParameter( ParameterType.GAIN );
		
		assertThat( gain, instanceOf( NeuronParam.Fixed.class ) );
		assertThat( ((Fixed) gain).getValue(), is( 2f ) );
	}
	
	@Test
	public void setsFixedWeight() throws Exception {
		Neuron neuron = layout.getNeuron( 0 );
		NeuronParam weight = neuron.getWeightParameter( 1 );
		assertThat( weight, instanceOf( NeuronParam.Fixed.class ));
		assertThat( ((Fixed) weight).getValue(), is( 1f ) );
	}
	
	@Test
	public void createsNeuronTauRangeCorrectly() throws Exception {
		Neuron neuron = layout.getNeuron( 0 );
		NeuronParam tau = neuron.getParameter( ParameterType.TAU );
		
		assertThat( tau, instanceOf( NeuronParam.Gene.class ) );
		NeuronParam.Gene tauG = (Gene) tau;
		assertThat( tauG.getRange().low, is( 0f ) );
		assertThat( tauG.getRange().high, is( 10f ) );
	}
	
	@Test
	public void createsNeuronGainRangeCorrectly() throws Exception {
		Neuron neuron = layout.getNeuron( 1 );
		
		NeuronParam gain = neuron.getParameter( ParameterType.GAIN );
		assertThat( gain, instanceOf( NeuronParam.Gene.class ) );
		NeuronParam.Gene gainG = (Gene) gain;
		assertThat( gainG.getRange().low, is( 10f ) );
		assertThat( gainG.getRange().high, is( 15f ) );
	}
	
	@Test
	public void createsNeuronWeightRangeCorrectly() throws Exception {
		Neuron neuron = layout.getNeuron( 0 );
		
		NeuronParam weight = neuron.getWeightParameter( 0 );
		
		assertThat( weight, instanceOf( NeuronParam.Gene.class ) );
		NeuronParam.Gene weightG = (Gene) weight;
		assertThat( weightG.getRange().low, is( 30f ) );
		assertThat( weightG.getRange().high, is( 40f ) );
		
	}
	
	@Test(expected = JSONNetworkParseError.class)
	public void throwsParseErrorOnNeuronNotFound() throws Exception {
		network.layers[0].neurons[0].conns[0] = "neuronThatDoesntExist";
		layout = network.getLayout();		
	}
	
	@Test
	public void createsConnection() throws Exception {
		Neuron neuron = layout.getNeuron( 1 );
		
		NeuronParam weight1 = neuron.getWeightParameter( 0 );
		assertThat( weight1, instanceOf( NeuronParam.Fixed.class ) );
		assertThat( ((Fixed)weight1).getValue(), is(0f) );
		

		NeuronParam weight2 = neuron.getWeightParameter( 2 );		
		assertThat( weight2, instanceOf( NeuronParam.Gene.class ) );		
	}
	
	@Test
	public void createsNeuronBiasRangeCorrectly() throws Exception {
		Neuron neuron = layout.getNeuron( 0 );
		
		NeuronParam bias = neuron.getParameter( ParameterType.BIAS );
		assertThat( bias, instanceOf( NeuronParam.Gene.class ) );
		NeuronParam.Gene biasG = (Gene) bias;
		assertThat( biasG.getRange().low, is( 10f ) );
		assertThat( biasG.getRange().high, is( 20f ) );
	}
	
	@Test
	public void putsAllRangesIntoMap() throws Exception {
		Map<String, ParamRange> rangeMap = Deencapsulation.invoke( network, "getRangeMap" );
		assertTrue( rangeMap.keySet().contains( "TestRange" ) );
		assertTrue( rangeMap.keySet().contains( "TestRange2" ) );
	}
	
	@Test( expected = JSONNetworkParseError.class )
	public void throwsParseErrorIfDuplicateRangeNames() throws Exception {
		network.ranges[1].name = "TestRange";
		layout = network.getLayout();
		Map<String, ParamRange> rangeMap = Deencapsulation.invoke( network, "getRangeMap" );
	}
	
	@Test( expected = JSONNetworkParseError.class )
	public void throwsParseErrorIfRangeNameNotFound() throws Exception {
		network.layers[0].neurons[0].range = "RangeNotInSpec";
		layout = network.getLayout();
		Map<String, ParamRange> rangeMap = Deencapsulation.invoke( network, "getRangeMap" );
	}
	
	@Test
	public void mapConnectionsMapsConnections() throws Exception {
		Map<String, Integer> connections = Deencapsulation.invoke(network, "mapConnections");
		assertThat(connections.get("s1"), is(0));
		assertThat(connections.get("s2"), is(1));
		assertThat(connections.get("m1"), is(2));
		assertThat(connections.get("m2"), is(3));
	}
	
	@Test
	public void mapsParametersToGenes() throws Exception {
		GeneMapping mapping = Deencapsulation.getField(layout, "mapping");
		
		assertThat(mapping.getGeneMapping(0).getMappings(), CoreMatchers.<Gene>hasItem((Gene)layout.getNeuron(0).getParameter(ParameterType.TAU)));
		assertThat(mapping.getGeneMapping(1).getMappings(), CoreMatchers.<Gene>hasItem((Gene)(layout.getNeuron(0).getParameter(ParameterType.BIAS))));
		assertThat(mapping.getGeneMapping(2).getMappings(), CoreMatchers.<Gene>hasItem((Gene)(layout.getNeuron(0).getWeightParameter(0))));
		assertThat(mapping.getGeneMapping(4).getMappings(), CoreMatchers.<Gene>hasItem((Gene)(layout.getNeuron(1).getWeightParameter(2))));
		assertThat(mapping.getGeneMapping(5).getMappings(), CoreMatchers.<Gene>hasItem((Gene)(layout.getNeuron(1).getParameter(ParameterType.GAIN))));
	}
	
//	@Test
	public void assertJSONParseCreatesEqualNetwork() throws Exception {
		assertThat( layout, is( equalTo( createLayout() ) ) );
	}	
	
	public CTRNNLayout createLayout() {
		CTRNNLayout layout = new CTRNNLayout();
		
		Neuron neuron1 = new Neuron(4);
		neuron1.setParameter(ParameterType.TAU, new NeuronParam.Gene(new Range(0,10)));
		neuron1.setParameter(ParameterType.BIAS, new NeuronParam.Gene(new Range(10,20)));
		neuron1.setParameter(ParameterType.GAIN, new NeuronParam.Fixed(2));
		neuron1.setWeight(0, new NeuronParam.Gene(new Range(30,40)));
		neuron1.setWeight(1, new NeuronParam.Fixed(1));
		
		Neuron neuron2 = new Neuron(4);
		neuron2.setParameter(ParameterType.TAU, new NeuronParam.Fixed(3));
		neuron2.setParameter(ParameterType.BIAS, new NeuronParam.Fixed(4));
		neuron2.setParameter(ParameterType.GAIN, new NeuronParam.Gene(new Range(10,15)));
		neuron2.setWeight(2, new NeuronParam.Gene(new Range(15,20)));
		neuron2.setWeight(3, new NeuronParam.Fixed(0.5f));
		
		Neuron neuron3 = new Neuron(4);
		neuron3.setParameter(ParameterType.TAU, new NeuronParam.Gene(new Range(0,10)));
		neuron3.setParameter(ParameterType.BIAS, new NeuronParam.Gene(new Range(10,20)));
		neuron3.setParameter(ParameterType.GAIN, new NeuronParam.Fixed(2));
		neuron3.setWeight(0, new NeuronParam.Gene(new Range(30,40)));
		neuron3.setWeight(1, new NeuronParam.Fixed(1));
		
		Neuron neuron4 = new Neuron(4);
		neuron2.setParameter(ParameterType.TAU, new NeuronParam.Fixed(3));
		neuron2.setParameter(ParameterType.BIAS, new NeuronParam.Fixed(4));
		neuron2.setParameter(ParameterType.GAIN, new NeuronParam.Gene(new Range(10,15)));
		neuron2.setWeight(2, new NeuronParam.Gene(new Range(15,20)));
		neuron2.setWeight(3, new NeuronParam.Fixed(0.5f));
		
		layout.addNeuron(neuron1);
		layout.addNeuron(neuron2);
		layout.addNeuron(neuron3);
		layout.addNeuron(neuron4);
		
		return layout;
	}
	
	
}
