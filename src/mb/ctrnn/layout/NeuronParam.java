package mb.ctrnn.layout;

/**
 * Represents a neural parameter, either fixed, or genetic.
 * 
 * @author Miles Bryant
 */
public interface NeuronParam {

	/**
	 * @return gets the current value of this parameter.
	 */
	public float getValue();

	/**
	 * Represents a parameter mapped by a gene. The value of the parameter is
	 * the gene value mapped by the range.
	 * 
	 * @author Miles Bryant
	 *
	 */
	public static class Gene implements NeuronParam {
		private Range range;
		private float geneValue = 0;

		/**
		 * Creates a new Gene parameter with the specified range.
		 * 
		 * @param range
		 *            range to map gene with.
		 */
		public Gene(Range range) {
			this.range = range;
		}

		/**
		 * @return value of gene (not mapped value).
		 */
		public float getGeneValue() {
			return geneValue;
		}

		/**
		 * Sets the value of the gene.
		 * 
		 * @param geneValue
		 *            Value of gene.
		 */
		public void setGeneValue(float geneValue) {
			this.geneValue = geneValue;
		}

		/**
		 * @return Value of the parameter (gene value mapped by range).
		 */
		@Override
		public float getValue() {
			return range.map(geneValue);
		}

		public Range getRange() {
			return range;
		}

		@Override
		public String toString() {
			return String.format(
					"[GeneParam range=%s geneValue=%.3f mappedValue=%.3f]",
					range, geneValue, getValue());
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof NeuronParam.Gene))
				return false;

			NeuronParam.Gene other = (NeuronParam.Gene) obj;
			return range.equals(other.range) && geneValue == other.geneValue;
		}
	}

	/**
	 * Represents a parameter with a fixed value.
	 * 
	 * @author Miles Bryant
	 */
	public static class Fixed implements NeuronParam {

		private final float value;

		/**
		 * Instantiates a new Fixed parameter with the specified value.
		 * 
		 * @param value
		 */
		public Fixed(float value) {
			this.value = value;
		}

		@Override
		public float getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "[FixedParam value=" + value + "]";
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof NeuronParam.Fixed))
				return false;

			NeuronParam.Fixed other = (NeuronParam.Fixed) obj;
			return this.value == other.value;
		}
	}
}
