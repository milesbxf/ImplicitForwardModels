package mb.util;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import mb.util.StatsUtils;

import org.junit.Test;

import com.google.common.collect.Lists;

public class StatsUtilsTest {

	final float[] 	ts1 = new float[] { 1,  3, 5, 7, 9,  7, 5, 3, 1, 3  }, 
					ts2 = new float[] { 2,  4, 6, 8, 10, 8, 6, 4, 2, 4  }, 
					ts3 = new float[] { 1,  2, 3, 4, 5,  6, 7, 8, 9, 10 }, 
					ts4 = new float[] { 10, 9, 8, 7, 6,  5, 4, 3, 2, 1  };
	final float[] 	tsMean = new float[] { 3.5f, 4.5f, 5.5f, 6.5f, 7.5f, 6.5f, 5.5f, 4.5f, 3.5f, 4.5f };
	
	
	final List<float[]> netOutputs = Lists.newArrayList( ts1,ts2,ts3,ts4 );
		
	final float[] 	var1 = new float[] { 6.25f, 2.25f, 0.25f, 0.25f, 2.25f, 0.25f, 0.25f, 2.25f, 6.25f, 2.25f }, 
					var2 = new float[] { 2.25f, 0.25f, 0.25f, 2.25f, 6.25f, 2.25f, 0.25f, 0.25f, 2.25f, 0.25f }, 
					var3 = new float[] { 6.25f, 6.25f, 6.25f, 6.25f, 6.25f, 0.25f, 2.25f, 12.25f, 30.25f, 30.25f },
					var4 = new float[] { 42.25f, 20.25f, 6.25f, 0.25f, 2.25f, 2.25f, 2.25f, 2.25f, 2.25f, 12.25f };
	final List<float[]>	tsVars = Lists.newArrayList( var1,var2,var3,var4 );
	
	@Test
	public void inputShouldRecordTimeSeriesMean() throws Exception {
		float[] expected = tsMean;
		float[] result = StatsUtils.calculateMeansOfArrays(netOutputs);
		
		assertThat(result, is(expected));		
	}
	
	@Test
	public void inputShouldUseMeanToCalcVariance() throws Exception {
		List<float[]> expected = tsVars;
		List<float[]> result = StatsUtils.calculateVarianceWithMeans(netOutputs,tsMean);
		
		for(int i = 0; i < tsVars.size(); i++) {
			assertThat(result.get( i ), is(expected.get( i )));
		}		
	}
	
	@Test
	public void inputReusesMeanToGetOverallMean() throws Exception {
		float expected = 5.95f;
		float result = StatsUtils.calculateMean(StatsUtils.calculateMeansOfArrays(tsVars));
		
		assertThat(result,is(expected));
		
	}
	
	
}
