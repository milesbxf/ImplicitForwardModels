package mb.ctrnn.json;



public class JSONTestUtils {

	public static Network createNetwork() {
		Network network = new Network();		
		network.metadata.author = "Miles";
		network.metadata.date = "June 2015";
		network.metadata.description = "Test";
		
		Network.ParamRange paramRange1 = new Network.ParamRange();
		paramRange1.name = "TestRange";
		paramRange1.tauRange = new Network.ParamRange.Range();
		paramRange1.tauRange.low = 0; paramRange1.tauRange.high = 10;
		paramRange1.biasRange = new Network.ParamRange.Range();
		paramRange1.biasRange.low = 10; paramRange1.biasRange.high = 20;
		paramRange1.gainRange = new Network.ParamRange.Range();
		paramRange1.gainRange.low = 20; paramRange1.gainRange.high = 30;
		paramRange1.weightRange = new Network.ParamRange.Range();
		paramRange1.weightRange.low = 30; paramRange1.weightRange.high = 40;
	
		Network.ParamRange paramRange2 = new Network.ParamRange();
		paramRange2.name = "TestRange2";
		paramRange2.tauRange = new Network.ParamRange.Range();
		paramRange2.tauRange.low = 0; paramRange2.tauRange.high = 5;
		paramRange2.biasRange = new Network.ParamRange.Range();
		paramRange2.biasRange.low = 5; paramRange2.biasRange.high = 10;
		paramRange2.gainRange = new Network.ParamRange.Range();
		paramRange2.gainRange.low = 10; paramRange2.gainRange.high = 15;
		paramRange2.weightRange = new Network.ParamRange.Range();
		paramRange2.weightRange.low = 15; paramRange2.weightRange.high = 20;
		
		network.ranges = new Network.ParamRange[2];
		network.ranges[0] = paramRange1;
		network.ranges[1] = paramRange2;
		
		Network.Layer layer1 = new Network.Layer();
		layer1.name = "Sensory";
		
		Network.Layer layer2 = new Network.Layer();
		layer2.name = "Motor";
		
		Network.Layer.Neuron neuron1 = new Network.Layer.Neuron();
		neuron1.name = "s1";
		neuron1.range = "TestRange";
		neuron1.tau = "g0";
		neuron1.bias = "g1";
		neuron1.gain = "2";
		neuron1.conns = new String[] {"s1","s2"};
		neuron1.weights = new String[] {"g2","1"};
		
	
		Network.Layer.Neuron neuron2 = new Network.Layer.Neuron();
		neuron2.name = "s2";
		neuron2.range = "TestRange2";
		neuron2.tau = "3";
		neuron2.bias = "4";
		neuron2.gain = "g5";
		neuron2.conns = new String[] {"m1","m2"};
		neuron2.weights = new String[] {"g4","0.5"};
		

		Network.Layer.Neuron neuron3 = new Network.Layer.Neuron();
		neuron3.name = "m1";
		neuron3.range = "TestRange";
		neuron3.tau = "g0";
		neuron3.bias = "g1";
		neuron3.gain = "2";
		neuron3.conns = new String[] {"s1","s2"};
		neuron3.weights = new String[] {"g2","1"};
		
	
		Network.Layer.Neuron neuron4 = new Network.Layer.Neuron();
		neuron4.name = "m2";
		neuron4.range = "TestRange2";
		neuron4.tau = "3";
		neuron4.bias = "4";
		neuron4.gain = "g5";
		neuron4.conns = new String[] {"m1","m2"};
		neuron4.weights = new String[] {"g4","0.5"};
				
		layer1.neurons = new Network.Layer.Neuron[2];
		layer1.neurons[0] = neuron1;
		layer1.neurons[1] = neuron2;
		

		layer2.neurons = new Network.Layer.Neuron[2];
		layer2.neurons[0] = neuron3;
		layer2.neurons[1] = neuron4;
		
		network.layers = new Network.Layer[2];
		network.layers[0] = layer1;
		network.layers[1] = layer2;
		
		return network;
	}

}
