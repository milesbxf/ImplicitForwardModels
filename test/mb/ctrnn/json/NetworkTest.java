package mb.ctrnn.json;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class NetworkTest {

	
	public void testJSONSerialise() throws JsonGenerationException, JsonMappingException, IOException {
		Network network = JSONTestUtils.createNetwork();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		network.writeJSONToStream(baos);
		String result = (baos.toString());
		
		System.out.println(result);
		
		assertEquals(serialised,result);
	}
	
	@Test
	public void testJSONDeserialise() throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(serialised.getBytes());		
		Network network = Network.fromInputStream(bais);
		assertEquals(network,JSONTestUtils.createNetwork());
	}

	public static final String serialised = "{\n" + 
			"  \"metadata\" : {\n" + 
			"    \"description\" : \"Test\",\n" + 
			"    \"author\" : \"Miles\",\n" + 
			"    \"date\" : \"June 2015\"\n" + 
			"  },\n" + 
			"  \"ranges\" : [ {\n" + 
			"    \"name\" : \"TestRange\",\n" + 
			"    \"tauRange\" : {\n" + 
			"      \"low\" : 0.0,\n" + 
			"      \"high\" : 10.0\n" + 
			"    },\n" + 
			"    \"biasRange\" : {\n" + 
			"      \"low\" : 10.0,\n" + 
			"      \"high\" : 20.0\n" + 
			"    },\n" + 
			"    \"gainRange\" : {\n" + 
			"      \"low\" : 20.0,\n" + 
			"      \"high\" : 30.0\n" + 
			"    },\n" + 
			"    \"weightRange\" : {\n" + 
			"      \"low\" : 30.0,\n" + 
			"      \"high\" : 40.0\n" + 
			"    }\n" + 
			"  }, {\n" + 
			"    \"name\" : \"TestRange2\",\n" + 
			"    \"tauRange\" : {\n" + 
			"      \"low\" : 0.0,\n" + 
			"      \"high\" : 5.0\n" + 
			"    },\n" + 
			"    \"biasRange\" : {\n" + 
			"      \"low\" : 5.0,\n" + 
			"      \"high\" : 10.0\n" + 
			"    },\n" + 
			"    \"gainRange\" : {\n" + 
			"      \"low\" : 10.0,\n" + 
			"      \"high\" : 15.0\n" + 
			"    },\n" + 
			"    \"weightRange\" : {\n" + 
			"      \"low\" : 15.0,\n" + 
			"      \"high\" : 20.0\n" + 
			"    }\n" + 
			"  } ],\n" + 
			"  \"layers\" : [ {\n" + 
			"    \"name\" : \"Sensory\",\n" + 
			"    \"neurons\" : [ {\n" + 
			"      \"name\" : \"s1\",\n" + 
			"      \"range\" : \"TestRange\",\n" + 
			"      \"tau\" : \"g0\",\n" + 
			"      \"bias\" : \"g1\",\n" + 
			"      \"gain\" : \"2\",\n" + 
			"      \"conns\" : [ \"s1\", \"s2\" ],\n" + 
			"      \"weights\" : [ \"g2\", \"1\" ]\n" + 
			"    }, {\n" + 
			"      \"name\" : \"s2\",\n" + 
			"      \"range\" : \"TestRange2\",\n" + 
			"      \"tau\" : \"3\",\n" + 
			"      \"bias\" : \"4\",\n" + 
			"      \"gain\" : \"g5\",\n" + 
			"      \"conns\" : [ \"m1\", \"m2\" ],\n" + 
			"      \"weights\" : [ \"g4\", \"0.5\" ]\n" + 
			"    } ]\n" + 
			"  }, {\n" + 
			"    \"name\" : \"Motor\",\n" + 
			"    \"neurons\" : [ {\n" + 
			"      \"name\" : \"m1\",\n" + 
			"      \"range\" : \"TestRange\",\n" + 
			"      \"tau\" : \"g0\",\n" + 
			"      \"bias\" : \"g1\",\n" + 
			"      \"gain\" : \"2\",\n" + 
			"      \"conns\" : [ \"s1\", \"s2\" ],\n" + 
			"      \"weights\" : [ \"g2\", \"1\" ]\n" + 
			"    }, {\n" + 
			"      \"name\" : \"m2\",\n" + 
			"      \"range\" : \"TestRange2\",\n" + 
			"      \"tau\" : \"3\",\n" + 
			"      \"bias\" : \"4\",\n" + 
			"      \"gain\" : \"g5\",\n" + 
			"      \"conns\" : [ \"m1\", \"m2\" ],\n" + 
			"      \"weights\" : [ \"g4\", \"0.5\" ]\n" + 
			"    } ]\n" + 
			"  } ]\n" + 
			"}";
	
}
