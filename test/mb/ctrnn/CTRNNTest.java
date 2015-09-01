package mb.ctrnn;

import static org.junit.Assert.assertArrayEquals;
import mb.ctrnn.core.CTRNN;
import mb.util.MathUtils;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

public class CTRNNTest {

	CTRNN ctrnn;
	@Mocked MathUtils mathUtils;
	int n = 2;		
	float[] taus = new float[n],
			biases = new float[n],
			gains = new float[n];
	float[][] weights = new float[n][n];
	
	@SuppressWarnings("javadoc")
	@Before
	public void setUp() {		
		    	
    	for (int i = 0; i < n; i++) {
			taus[i] = 1f;biases[i]=1f;gains[i]=1f;
			for (int j = 0; j < n; j++) {
				weights[i][j] = 1f;
			}
		}
		
		ctrnn = new CTRNN(n, taus, biases, gains, weights);
	}
	
	@Test
	public void CTRNNSetUpCorrectly() {
		/*
		 * taus, gains, biases should all be set to 1 in setUp(),
		 * whilst states all = 0.5 and outputs = 0.
		 * Weights = [[0,1][0,1]].
		 */		
//		
//		float[] expected = new float[]{1,1},
//				result = Deencapsulation.getField(ctrnn, "taus");
//		assertArrayEquals(expected, result,0.001f);		
//		
//		expected = new float[]{1,1};
//		result = Deencapsulation.getField(ctrnn, "gains");
//		assertArrayEquals(expected, result,0.001f);
//		
//		expected = new float[]{1,1};
//		result = Deencapsulation.getField(ctrnn, "biases");
//		assertArrayEquals(expected, result,0.001f);
		
		float[]expected = new float[]{0.5f,0.5f},
		result = Deencapsulation.getField(ctrnn, "states");
		assertArrayEquals(expected, result,0.001f);

		expected = new float[]{0f,0f};
		result = Deencapsulation.getField(ctrnn, "outputs");
		assertArrayEquals(expected, result,0.001f);
		
//
//		float[][] expectedWeights = new float[][]{{0f,1f},{0f,1f}},
//				resultWeights = Deencapsulation.getField(ctrnn, "weights");
//		for(int i = 0; i < 2; i++)
//			assertArrayEquals(expectedWeights[i], resultWeights[i],0.001f);	
	}
	
	@SuppressWarnings("javadoc")
	@Test
	public void CTRNNstepsCorrectly() throws Exception {
		/**
		 * Outputs should initially equal 0, then equal 1 after stepping.
		 * The sigmoid function is mocked to return 1.
		 */
		new Expectations() {{
			MathUtils.sigmoid(anyFloat); result = 1f; result = 1f;
		}};

		float[] expected = new float[]{0f,0f},
		result = Deencapsulation.getField(ctrnn, "outputs");
		assertArrayEquals(expected, result,0.001f);
		
		ctrnn.step(1, new float[]{0,0});
		
		expected = new float[]{1f,1f};
		result = Deencapsulation.getField(ctrnn, "outputs");
		assertArrayEquals(expected, result,0.001f);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void throwsIllegalArgumentExceptionIfIncorrectArrayLength() throws Exception {
		taus = new float[] {1f,1f,1f};
		ctrnn = new CTRNN(n, taus, biases, gains, weights);
	}

}
