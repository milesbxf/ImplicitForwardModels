package mb.ctrnn.layout;

import java.util.ArrayList;
import java.util.List;

import mb.ctrnn.layout.NeuronParam.Gene;

/**
 * Maps between genes and parameters. It does not know or care which parameters
 * are which; it merely updates the values.
 * 
 * @author miles
 *
 */
public class GeneMapping {

	private final List<Mapping> genes;

	/**
	 * Creates a new empty gene mapping.
	 */
	public GeneMapping() {
		genes = new ArrayList<>();
	}

	/**
	 * Sets a new gene mapping at the specified index.
	 * 
	 * @param geneIndex
	 *            Index of gene to map. If index is higher than the current
	 *            number of genes, a null value will be added to the array until
	 *            there are enough genes.
	 * @param gene
	 *            Parameter to map to. Should be added to a neuron or the gene
	 *            will have no effect
	 */
	public void setNewGeneMapping(int geneIndex, NeuronParam.Gene gene) {
		while (geneIndex >= genes.size()) {
			genes.add(new Mapping());
		}
		genes.get(geneIndex).add(gene);
		genes.get(geneIndex).setGeneValue(gene.getGeneValue());
	}

	/**
	 * Gets the actual value of the gene at the specified index.
	 * 
	 * @param index
	 *            index of gene.
	 * @return float of the current gene value at that index.
	 */
	public float getValueAtGene(int index) {
		return genes.get(index).getValue();
	}

	public Mapping getGeneMapping(int index) {
		return genes.get(index);
	}
	
	/**
	 * Sets the actual value of the gene at the specified index.
	 * 
	 * @param index
	 *            index of gene.
	 * @param value
	 *            float of the current gene value at that index to set.
	 */
	public void setGeneValueAt(int index, float value) {
		genes.get(index).setGeneValue(value);
	}

	/**
	 * Sets the actual values of all the genes in this mapping.
	 * 
	 * @param geneValues
	 *            Array of float values to set genes with. Should be same length
	 *            as the number of genes in this mapping or an IndexOutOfBounds
	 *            exception will be thrown. If any genes are not yet mapped,
	 *            i.e. their mapping is null, a NullPointer exception is thrown.
	 */
	public void setAllGenes(float[] geneValues) {
		for (int i = 0; i < geneValues.length; i++) {
			setGeneValueAt(i, geneValues[i]);
		}
	}

	/**
	 * Gets the total number of genes in this mapping, including any mappings not set yet.
	 * @return int number of gene mappings.
	 */
	public int getTotalGenes() {
		return genes.size();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof GeneMapping))
			return false;
		
		GeneMapping other = (GeneMapping)obj;
		for(int i = 0; i < genes.size(); i++) {
			if(!genes.get(i).equals(other.genes.get(i)))
				return false;
		}
		
		return true;
	}
	
	public class Mapping {
		private float geneValue;
		
		private List<NeuronParam.Gene> mappings;
		
		public Mapping() {
			geneValue = 0;
			mappings = new ArrayList<>();
		}

		public void setGeneValue(float value) {
			geneValue = value;
			for(NeuronParam.Gene gene : mappings) {
				gene.setGeneValue( value );
			}
		}

		public float getValue() {
			return geneValue;
		}

		public void add(Gene gene) {
			mappings.add(gene);
		}
		
		public List<NeuronParam.Gene> getMappings() {
			return mappings;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof Mapping))
				return false;
			Mapping other = (Mapping)obj;
			if(geneValue != other.geneValue)
				return false;
			if(mappings.size() != other.mappings.size())
				return false;
			for(int i = 0; i < mappings.size(); i++) {
				if(!mappings.get(i).equals(other.mappings.get(i)))
					return false;
			}
			return true;
		}
	}
	
}
