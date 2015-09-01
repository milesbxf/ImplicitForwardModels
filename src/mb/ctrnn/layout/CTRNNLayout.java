package mb.ctrnn.layout;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mb.ctrnn.core.CTRNN;
import mb.ctrnn.json.Network;
import mb.ctrnn.json.Network.JSONNetworkParseError;
import mb.ctrnn.layout.Neuron.ParameterType;

/**
 * The CTRNNLayout class provides convenient methods to set up a CTRNN
 * programmatically, e.g. for parsing from a JSON file. It also allows a mapping
 * between genes and parameters.
 * 
 * @author Miles Bryant
 *
 */
public class CTRNNLayout {
	private List<Neuron>	neurons;
	private GeneMapping		mapping;
	
	public static CTRNNLayout fromFileName(String filename) throws FileNotFoundException, JSONNetworkParseError, IOException {
		return Network.fromInputStream( new FileInputStream(filename) ).getLayout();
	}
	
	/**
	 * Constructs a new empty CTRNNLayout.
	 */
	public CTRNNLayout () {
		neurons = new ArrayList<>();
		mapping = new GeneMapping();
	}
	
	/**
	 * Creates a gene mapping between a gene index and a parameter.
	 * 
	 * @param geneIndex
	 *            Index of gene to map.
	 * @param gene
	 *            Parameter to map to. Should be added to a neuron or the gene
	 *            will have no effect
	 */
	public void setGeneAt( int geneIndex, NeuronParam.Gene gene ) {
		mapping.setNewGeneMapping( geneIndex, gene );
	}
	
	/**
	 * Adds a neuron to the layout.
	 * 
	 * @param neuron
	 *            Neuron to add to the layout.
	 */
	public void addNeuron( Neuron neuron ) {
		neurons.add( neuron );
	}
	
	/**
	 * Gets a neuron from the layout.
	 * 
	 * @param index
	 *            Index of neuron to get.
	 * @return the Layout neuron at the specified location.
	 */
	public Neuron getNeuron( int index ) {
		return neurons.get( index );
	}
	
	public int getNumberOfNeurons() {
		return neurons.size();
	}
	
	public int getNumberOfGenes() {
		return mapping.getTotalGenes();
	}
	
	/**
	 * Sets the gene values with the specified gene array.
	 * 
	 * @param genes
	 *            Float array of gene values to set. Must be same number of
	 *            genes as exist in the mapping.
	 */
	public void updateGenes( float[] genes ) {
		mapping.setAllGenes( genes );
	}
	
	/**
	 * Creates a CTRNN for simulation using the neurons in the layout and their
	 * parameters.
	 * 
	 * @return a CTRNN network
	 */
	public CTRNN createCTRNN() {
		int n = neurons.size();
		float[] taus = new float[n];
		float[] biases = new float[n];
		float[] gains = new float[n];
		float[][] weights = new float[n][n];
		
		for ( int i = 0; i < n; i++ ) {
			Neuron neuron = neurons.get( i );
			if ( !neuron.areAllParametersSet() )
				throw new IllegalStateException( "Some parameters weren't set for neuron " + neuron.toString() );
			
			taus[i] = neuron.getValue( ParameterType.TAU );
			biases[i] = neuron.getValue( ParameterType.BIAS );
			gains[i] = neuron.getValue( ParameterType.GAIN );
			for ( int j = 0; j < n; j++ ) {
				weights[i][j] = neuron.getWeight( j );
			}
		}
		
		CTRNN ctrnn = new CTRNN( n, taus, biases, gains, weights );
		return ctrnn;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CTRNNLayout))
			return false;
		
		CTRNNLayout other = (CTRNNLayout)obj;
		
		if(!mapping.equals(other.mapping))
			return false;
		
		for(int i = 0; i < neurons.size(); i++) {
			if(neurons.get(i).equals(other.neurons.get(i)))
				return false;
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		return "[CTRNNLayout neurons=" + neurons.size() + " genes=" + mapping.getTotalGenes() + "]";
	}
}
