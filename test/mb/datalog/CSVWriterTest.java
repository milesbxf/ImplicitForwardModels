package mb.datalog;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.google.common.collect.Lists;

public class CSVWriterTest {

	@Test
	public void writingSingleColumnOfDataWritesCorrectly() throws Exception {
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		CSVWriter.CSVParamBuilder builder = new CSVWriter.CSVParamBuilder();
		builder.addHeader("Header1");
		builder.addSingleDatum("Data1");
		CSVWriter.writeToCSVOutputStream(baOS, builder);
		String result = new String(baOS.toByteArray());
		String expected = "Header1\nData1\n";
		assertThat(result,is(expected));
	}
	
	@Test
	public void writingMultipleColumnsOfDataWritesCorrectly() throws Exception {
		ByteArrayOutputStream baOS = new ByteArrayOutputStream();
		CSVWriter.CSVParamBuilder builder = new CSVWriter.CSVParamBuilder();
		builder.addHeader("Header1");builder.addHeader("Header2");builder.addHeader("Header3");
		builder.addDataRow(Lists.newArrayList((Object)"data1",(Object)"data2",(Object)"data3"));
		CSVWriter.writeToCSVOutputStream(baOS, builder);
		String result = new String(baOS.toByteArray());
		String expected = "Header1\tHeader2\tHeader3\ndata1\tdata2\tdata3\n";
		assertThat(result,is(expected));
	}

}
