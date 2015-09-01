package mb.datalog;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class DataLoggerTest {

	DataLogger dl;
	@Mocked
	Loggable loggable;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void initialisingSetsHeaders() {
		new Expectations() {
		{
			loggable.getHeaders(); result= Lists.newArrayList("h1","h2","h3","h4");result= Lists.newArrayList("h1","h2","h3","h4");
		}
	};
		dl = new DataLogger(loggable);
		List<String> result = dl.getHeaders();
		List<String> expected = new LinkedList<>();
		expected.add("h1");
		expected.add("h2");
		expected.add("h3");
		expected.add("h4");
		assertThat(result, is(expected));
	}

	@Test
	public void appendDataAppendsDataFromLoggable() throws Exception {
		new Expectations() {
			{
				loggable.getHeaders(); result= Lists.newArrayList("h1");
				loggable.getData();result=Lists.newArrayList("test");
			}
		};

		dl = new DataLogger(loggable);
		dl.appendData();
		List<List<Object>> result=dl.getData();
		List<List<Object>> expected = new LinkedList<>();
		expected.add(Lists.newArrayList((Object)"test"));
		assertThat(result,is(expected));		
	}

	@Test(expected=IllegalArgumentException.class)
	public void incorrectAmountOfDataThrowsException() throws Exception {
		new Expectations() {
			{
				loggable.getHeaders(); result= Lists.newArrayList("h1","h2");result= Lists.newArrayList("h1","h2");
				loggable.getData();result=Lists.newArrayList("test");
			}
		};
		dl = new DataLogger(loggable);
		dl.appendData();
	}
	
}
