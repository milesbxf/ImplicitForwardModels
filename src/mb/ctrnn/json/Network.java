package mb.ctrnn.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import mb.ctrnn.json.Network.Layer.Neuron;
import mb.ctrnn.layout.CTRNNLayout;
import mb.ctrnn.layout.Neuron.ParameterType;
import mb.ctrnn.layout.NeuronParam;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Network is a POJO for extracting data out of JSON files with JacksonCSV. Once
 * in POJO format it can then be converted to a CTRNNLayout instance, which can
 * create CTRNNs.
 * 
 * @author Miles Bryant <mb459@sussex.ac.uk>
 *
 */
public class Network {
	/**
	 * Metadata for storing author,date,description
	 */
	public Metadata		metadata;
	/**
	 * Parameter mapping ranges specifying how [-1,1] genes are mapped to
	 * parameters
	 */
	public ParamRange[]	ranges;
	/**
	 * Array of Layers which contain neurons
	 */
	public Layer[]		layers;
	
	/**
	 * Creates a new blank Network
	 */
	public Network () {
		metadata = new Metadata();
	}
	
	/**
	 * Metadata for storing author,date,description
	 * 
	 * @author Miles Bryant <mb459@sussex.ac.uk>
	 */
	public static class Metadata {
		public String	description, author, date;
		
		@Override
		public boolean equals( Object obj ) {
			if ( !(obj instanceof Metadata) )
				return false;
			Metadata other = (Metadata) obj;
			return (description.equals( other.description )
					&& author.equals( other.author ) && date.equals( date ));
		}
	}
	
	/**
	 * Parameter mapping ranges specifying how [-1,1] genes are mapped to
	 * parameters
	 * 
	 * @author Miles Bryant <mb459@sussex.ac.uk>
	 */
	public static class ParamRange {
		public String	name;
		public Range	tauRange, biasRange, gainRange, weightRange;
		
		@Override
		public boolean equals( Object obj ) {
			if ( !(obj instanceof ParamRange) )
				return false;
			ParamRange other = (ParamRange) obj;
			return (name.equals( other.name ) && tauRange.equals( other.tauRange )
					&& biasRange.equals( biasRange ) && gainRange.equals( gainRange )
					&& weightRange.equals( weightRange ));
		}
		
		/**
		 * 
		 * 
		 * @author Miles Bryant <mb459@sussex.ac.uk>
		 *
		 */
		public static class Range {
			public float	low, high;
			
			@Override
			public boolean equals( Object obj ) {
				if ( !(obj instanceof Range) )
					return false;
				Range other = (Range) obj;
				return (low == other.low) && (high == other.high);
			}
			
			public mb.ctrnn.layout.Range toLayoutRange() {
				return new mb.ctrnn.layout.Range( low, high );
			}
		}
	}
	
	public static class Layer {
		public String	name;
		public Neuron[]	neurons;
		
		@Override
		public boolean equals( Object obj ) {
			if ( !(obj instanceof Layer) )
				return false;
			Layer other = (Layer) obj;
			if ( !name.equals( other.name ) )
				return false;
			for ( int i = 0; i < neurons.length; i++ ) {
				if ( !neurons[i].equals( other.neurons[i] ) )
					return false;
			}
			return true;
		}
		
		public static class Neuron {
			public String	name;
			public String	range;
			public String	tau;
			public String	bias;
			public String	gain;
			public String[]	conns;
			public String[]	weights;
			
			@Override
			public boolean equals( Object obj ) {
				if ( !(obj instanceof Neuron) )
					return false;
				Neuron other = (Neuron) obj;
				if ( !name.equals( other.name )
						|| !range.equals( other.range )
						|| !tau.equals( other.tau )
						|| !bias.equals( other.bias )
						|| !gain.equals( other.gain ) )
					return false;
				for ( int i = 0; i < conns.length; i++ ) {
					if ( !conns[i].equals( other.conns[i] )
							|| !weights[i].equals( other.weights[i] ) )
						return false;
				}
				return true;
			}
		}
		
	}
	
	@Override
	public boolean equals( Object obj ) {
		if ( !(obj instanceof Network) )
			return false;
		Network other = (Network) obj;
		if ( !metadata.equals( other.metadata ) )
			return false;
		for ( int i = 0; i < ranges.length; i++ ) {
			if ( !ranges[i].equals( other.ranges[i] ) )
				return false;
		}
		for ( int i = 0; i < layers.length; i++ ) {
			if ( !layers[i].equals( other.layers[i] ) )
				return false;
		}
		return true;
	}
	
	/**
	 * Writes JSON to an OutputStream.
	 * 
	 * @param out
	 *            OutputStream to stream JSON to.
	 * @throws JsonGenerationException
	 *             Thrown by JacksonCSV if problem with mapping.
	 * @throws JsonMappingException
	 *             Thrown by JacksonCSV if problem with mapping.
	 * @throws IOException
	 *             Thrown if I/O problem with stream.
	 */
	public void writeJSONToStream( OutputStream out )
			throws JsonGenerationException, JsonMappingException, IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		// mapper.setVisibility( mapper.getSerializationConfig()
		// .getDefaultVisibilityChecker()
		// .withFieldVisibility( Visibility.ANY )
		// .withGetterVisibility( Visibility.NONE )
		// .withSetterVisibility( Visibility.NONE )
		// .withCreatorVisibility( Visibility.NONE ) );
		
		mapper.configure( SerializationFeature.INDENT_OUTPUT, true );
		mapper.configure( SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true );
		mapper.setSerializationInclusion( Include.ALWAYS );
		mapper.writeValue( out, this );
	}
	
	/**
	 * @return a CTRNNLayout with the parameters specified by this object.
	 * @throws JSONNetworkParseError
	 *             if there is a problem with parsing the JSON file to Network.
	 */
	public CTRNNLayout getLayout() throws JSONNetworkParseError {
		
		CTRNNLayout layout = new CTRNNLayout();
		
		int nNeurons = getNumberOfNeurons();
		
		Map<String, ParamRange> rangeMap = getRangeMap();
		Map<String, Integer> connections = mapConnections();
		
		// /loop through network layers and neurons and add them to the layout
		
		for ( Layer layer : this.layers ) {
			for ( Neuron jsonNeuron : layer.neurons ) {
				mb.ctrnn.layout.Neuron layoutNeuron = new mb.ctrnn.layout.Neuron( nNeurons );
				
				if ( !rangeMap.containsKey( jsonNeuron.range ) ) {
					throw new JSONNetworkParseError( "Range " + jsonNeuron.range + " not found in file" );
				}
				
				ParamRange jsonRange = rangeMap.get( jsonNeuron.range );
				
				NeuronParam tauParam = null;
				if ( isGene( jsonNeuron.tau ) ) {
					tauParam = new NeuronParam.Gene( jsonRange.tauRange.toLayoutRange() );
					layout.setGeneAt( parseGeneNumber( jsonNeuron.tau ), (NeuronParam.Gene) tauParam );
				} else {
					tauParam = new NeuronParam.Fixed( parseFloat( jsonNeuron.tau ) );
				}
				layoutNeuron.setParameter( ParameterType.TAU, tauParam );
				
				NeuronParam biasParam = null;
				if ( isGene( jsonNeuron.bias ) ) {
					biasParam = new NeuronParam.Gene( jsonRange.biasRange.toLayoutRange() );
					layout.setGeneAt( parseGeneNumber( jsonNeuron.bias ), (NeuronParam.Gene) biasParam );
				} else {
					biasParam = new NeuronParam.Fixed( parseFloat( jsonNeuron.bias ) );
				}
				layoutNeuron.setParameter( ParameterType.BIAS, biasParam );
				
				NeuronParam gainParam = null;
				if ( isGene( jsonNeuron.gain ) ) {
					gainParam = new NeuronParam.Gene( jsonRange.gainRange.toLayoutRange() );
					layout.setGeneAt( parseGeneNumber( jsonNeuron.gain ), (NeuronParam.Gene) gainParam );
				} else {
					gainParam = new NeuronParam.Fixed( parseFloat( jsonNeuron.gain ) );
				}
				layoutNeuron.setParameter( ParameterType.GAIN, gainParam );
				
				if ( jsonNeuron.conns.length != jsonNeuron.weights.length )
					throw new JSONNetworkParseError( "Connections (length "
							+ jsonNeuron.conns.length + ") should be same length as weights (length " + jsonNeuron.weights.length + ")" );
				
				for ( int i = 0; i < jsonNeuron.weights.length; i++ ) {
					String connN = jsonNeuron.conns[i];
					
					NeuronParam weightParam = null;
					if ( isGene( jsonNeuron.weights[i] ) ) {
						weightParam = new NeuronParam.Gene( jsonRange.weightRange.toLayoutRange() );
						layout.setGeneAt( parseGeneNumber( jsonNeuron.weights[i] ), (NeuronParam.Gene) weightParam );
					} else {
						weightParam = new NeuronParam.Fixed( parseFloat( jsonNeuron.weights[i] ) );
					}
					
					if ( !connections.containsKey( connN ) )
						throw new JSONNetworkParseError( "Neuron " + connN + " not found" );
					
					int connIndex = connections.get( connN );
					layoutNeuron.setWeight( connIndex, weightParam );
				}
				
				layout.addNeuron( layoutNeuron );
			}
		}
		return layout;
	}
	
	private int parseGeneNumber( String value ) throws JSONNetworkParseError {
		try {
			return Integer.valueOf( value.substring( 1 ) );
		} catch ( NumberFormatException e ) {
			throw new JSONNetworkParseError( "Value " + value + "cannot be parsed to a gene index" );
		}
	}
	
	private float parseFloat( String value ) throws JSONNetworkParseError {
		try {
			return Float.valueOf( value );
		} catch ( NumberFormatException e ) {
			throw new JSONNetworkParseError( "Value " + value + "cannot be parsed to a float value" );
		}
	}
	
	private boolean isGene( String spec ) {
		return spec.startsWith( "g" );
	}
	
	private Map<String, ParamRange> getRangeMap() throws JSONNetworkParseError {
		Map<String, ParamRange> rangeMap = new HashMap<>();
		
		for ( ParamRange range : this.ranges ) {
			if ( rangeMap.containsKey( range.name ) )
				throw new JSONNetworkParseError( "Range " + range.name + " is defined more than once" );
			else {
				rangeMap.put( range.name, range );
			}
		}
		return rangeMap;
	}
	
	private Map<String, Integer> mapConnections() throws JSONNetworkParseError {
		Map<String, Integer> connections = new HashMap<>();
		int counter = 0;
		for ( Layer layer : this.layers ) {
			for ( Neuron neuron : layer.neurons ) {
				if ( connections.containsKey( neuron.name ) )
					throw new JSONNetworkParseError( "Neuron " + neuron.name + " is defined more than once" );
				else {
					connections.put( neuron.name, counter );
				}
				counter++;
			}
		}
		return connections;
	}
	
	/**
	 * @return the total number of neurons in all layers.
	 */
	public int getNumberOfNeurons() {
		int sum = 0;
		for ( Layer layer : this.layers ) {
			sum += layer.neurons.length;
		}
		return sum;
	}
	
	/**
	 * Loads Network from JSON from the specified stream.
	 * 
	 * @param input
	 *            InputStream to stream from.
	 * @return a Network parsed from the JSON.
	 * @throws IOException
	 *             if there is a problem with the stream.
	 */
	public static Network fromInputStream( InputStream input )
			throws IOException {
		
		ObjectMapper mapper = new ObjectMapper();
		// mapper.setVisibility( mapper.getDeserializationConfig()
		// .getDefaultVisibilityChecker()
		// .withFieldVisibility( Visibility.ANY )
		// .withGetterVisibility( Visibility.NONE )
		// .withSetterVisibility( Visibility.NONE )
		// .withCreatorVisibility( Visibility.NONE ) );
		
		return mapper.readValue( input, Network.class );
	}
	
	/**
	 * Thrown if an error with the JSON network file is found. Not thrown for
	 * JSON syntax errors (which are handled by Jackson) but for incorrect
	 * formats, like a number not being correctly parsed.
	 * 
	 * @author Miles Bryant <mb459@sussex.ac.uk>
	 *
	 */
	public class JSONNetworkParseError extends Exception {
		
		public JSONNetworkParseError () {
			// TODO Auto-generated constructor stub
		}
		
		public JSONNetworkParseError ( String message ) {
			super( message );
		}
		
	}
}
