package eu.eexcess.insa.proxy.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.script.ScriptException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.retry.ExhaustedRetryException;

import eu.eexcess.insa.oauth.OAuthSigningProcessor;
import eu.eexcess.insa.proxy.APIService;
import eu.eexcess.insa.proxy.actions.ApplyPrivacySettingsJS;

public class ApplyPrivacySettingsTest extends CamelTestSupport {
	//http://tools.ietf.org/html/rfc5849
	
	
	
	
    @Produce(uri = "string-template:templates/profile.tm")
    protected ProducerTemplate template;
	private Processor prepMessageProcessor = new Processor() {
		public void process(Exchange exchange) throws Exception {
			Message in = exchange.getIn();
			// the input message will contain a raw elasticsearch 
			//response about a user's profile, stored into the exchange property : user_context-profile
			final InputStream testUserProfile= ClassLoader.getSystemResourceAsStream("data/mendeleyQueryTest_InputBody.json");
			exchange.setProperty("user_context-profile", testUserProfile);

		}
	};
	
	
	public ArrayList<HashMap<String,String>> addressTestDataGenerator() throws IOException{
		
		ArrayList<HashMap<String,String>> inputData = new ArrayList<HashMap<String,String>>();
		
		HashMap<String, String> address1 = new HashMap<String, String> ( );
		
		HashMap<String, String> address1Data = new HashMap<String,String>();
		address1.put("0",formateAddress(address1Data));
		
		address1Data.put("country", "France");
		address1.put("1",formateAddress(address1Data));
		
		address1Data.put("region", "Rhône-Alpes");
		address1.put("2",formateAddress(address1Data));
		
		address1Data.put("district", "Région de Lyon");
		address1.put("3",formateAddress(address1Data));
		
		address1Data.put("city", "Villeurbanne");
		address1Data.put("postalcode", "69100");
		address1.put("4",formateAddress(address1Data));
		
		address1Data.put("street", "42 rue de la Liberté");
		address1Data.put("lattitude", "41.785110");
		address1Data.put("longitude", "23.889504");
		address1.put("5",formateAddress(address1Data));
		
		address1.put("raw", formateAddress(address1Data));
		
		inputData.add(address1);
		
		return inputData;
		
		
		
	    
	}
	
	String formateAddress ( HashMap<String, String> address) throws IOException{
		
		JsonFactory factory = new JsonFactory();
		StringWriter sWriter = new StringWriter();
		JsonGenerator jg = factory.createJsonGenerator(sWriter);
		
		jg.writeStartObject();
		if ( !address.isEmpty()){
			
			if ( address.containsKey("country")){
				jg.writeStringField("country", address.get("country"));
			}
			if ( address.containsKey("region")){
				jg.writeStringField("region", address.get("region"));
			}
			if ( address.containsKey("district")){
				jg.writeStringField("district", address.get("district"));
			}
			if ( address.containsKey("city")){
				jg.writeStringField("city", address.get("city"));
			}
			if ( address.containsKey("postalcode")){
				jg.writeStringField("postalcode", address.get("postalcode"));
			}
			if ( address.containsKey("street")){
				jg.writeStringField("street", address.get("street"));
			}
			if ( address.containsKey("longitude")){
				jg.writeStringField("longitude", address.get("longitude"));
			}
			if ( address.containsKey("lattitude")){
				jg.writeStringField("lattitude", address.get("lattitude"));
			}
			
		}

		jg.writeEndObject();
		jg.close();
		return sWriter.toString();
	}
	
	
	public ArrayList<HashMap<String,String>> birthDateTestDataGenerator(){
		
		ArrayList<HashMap<String,String>> inputData = new ArrayList<HashMap<String,String>>();
			
		HashMap<String,String> date1 = new HashMap<String, String>();
			date1.put("raw", "1992-06-03");
			date1.put("3", "{\"date\":\"1992-06-03\"}");
			date1.put("2", "{\"age\":\"21 years\"}");
			date1.put("1", "{\"decade\":\"20\"}");
			date1.put("0", "nothing");
			inputData.add(date1);
			
		HashMap<String,String> date2 = new HashMap<String, String>();
			date2.put("raw", "1982-12-3");
			date2.put("3", "{\"date\":\"1982-12-3\"}");
			date2.put("2", "{\"age\":\"31 years\"}");
			date2.put("1", "{\"decade\":\"30\"}");
			date2.put("0", "nothing");
			inputData.add(date2);
		return inputData;	
	}
	
	
	public ArrayList<HashMap<String,String>> emailTestDataGenerator(){
		
		ArrayList<HashMap<String,String>> inputData = new ArrayList<HashMap<String,String>>();
			
		HashMap<String,String> email1 = new HashMap<String, String>();
			email1.put("raw", "email.test@test.fr");
			email1.put("1", "email.test@test.fr");
			email1.put("0", "nothing");
			inputData.add(email1);
			
		HashMap<String,String> email2 = new HashMap<String, String>();
			email2.put("raw", "email");
			email2.put("1", "email");
			email2.put("0", "nothing");
			inputData.add(email2);
			
		return inputData;	
	}
	
	public ArrayList<HashMap<String,String>> titleTestDataGenerator(){
		
		ArrayList<HashMap<String,String>> inputData = new ArrayList<HashMap<String,String>>();
			
		HashMap<String,String> email1 = new HashMap<String, String>();
			email1.put("raw", "Mister");
			email1.put("1", "Mister");
			email1.put("0", "nothing");
			inputData.add(email1);
			
		HashMap<String,String> title2 = new HashMap<String, String>();
			title2.put("raw", "Miss");
			title2.put("1", "Miss");
			title2.put("0", "nothing");
			inputData.add(title2);
			
		HashMap<String,String> title3 = new HashMap<String, String>();
			title3.put("raw", "Misses");
			title3.put("1", "Misses");
			title3.put("0", "nothing");
			inputData.add(title3);
			
		return inputData;	
	}
	
	
public ArrayList<HashMap<String,String>> genderTestDataGenerator(){
		
		ArrayList<HashMap<String,String>> inputData = new ArrayList<HashMap<String,String>>();
			
		HashMap<String,String> gender1 = new HashMap<String, String>();
			gender1.put("raw", "Male");
			gender1.put("1", "Male");
			gender1.put("0", "nothing");
			inputData.add(gender1);
			
		HashMap<String,String> gender2 = new HashMap<String, String>();
			gender2.put("raw", "Female");
			gender2.put("1", "Female");
			gender2.put("0", "nothing");
			inputData.add(gender2);
			
		return inputData;	
	}
/*
    @Test
    public void test() throws Exception {
    	
    	
    	Exchange resp = template.send(prepMessageProcessor);
		String filtered= resp.getProperty("user_context-profile",String.class);
		
		
		
	    
		
    }
    */
    
    @Test
    public void test_applyPrivacy_birthdate() throws ScriptException{
    	ApplyPrivacySettingsJS privacy = new ApplyPrivacySettingsJS();
		ArrayList<HashMap<String,String>> inputDatas = birthDateTestDataGenerator();
		Iterator<HashMap<String,String>> it =inputDatas.iterator();
		
		while ( it.hasNext()){
			HashMap<String,String> h = it.next();
			String field="birthdate";
		
			assertEquals( h.get("0"),privacy.applyPrivacy(field, h.get("raw"), 0) );
			assertEquals(  h.get("1"),privacy.applyPrivacy(field, h.get("raw"), 1));
			assertEquals( h.get("2"), privacy.applyPrivacy(field, h.get("raw"), 2));
			assertEquals( h.get("3"),privacy.applyPrivacy(field, h.get("raw"), 3));
			
		}
    }
    
    @Test
    public void test_applyPrivacy_email() throws ScriptException{
    	ApplyPrivacySettingsJS privacy = new ApplyPrivacySettingsJS();
		ArrayList<HashMap<String,String>> inputDatas = emailTestDataGenerator();
		Iterator<HashMap<String,String>> it =inputDatas.iterator();
		String field ="email";
		while ( it.hasNext()){
			HashMap<String,String> h = it.next();
			assertEquals( h.get("0"),privacy.applyPrivacy(field, h.get("raw"), 0) );
			assertEquals(  h.get("1"),privacy.applyPrivacy(field, h.get("raw"), 1));
		}
    }
    
    @Test
    public void test_applyPrivacy_title() throws ScriptException{
    	ApplyPrivacySettingsJS privacy = new ApplyPrivacySettingsJS();
		ArrayList<HashMap<String,String>> inputDatas = titleTestDataGenerator();
		Iterator<HashMap<String,String>> it =inputDatas.iterator();
		String field ="title";
		while ( it.hasNext()){
			HashMap<String,String> h = it.next();
			assertEquals( h.get("0"),privacy.applyPrivacy(field, h.get("raw"), 0) );
			assertEquals(  h.get("1"),privacy.applyPrivacy(field, h.get("raw"), 1));
		}
    }
    
    @Test
    public void test_applyPrivacy_gender() throws ScriptException{
    	ApplyPrivacySettingsJS privacy = new ApplyPrivacySettingsJS();
		ArrayList<HashMap<String,String>> inputDatas = genderTestDataGenerator();
		Iterator<HashMap<String,String>> it =inputDatas.iterator();
		String field ="gender";
		while ( it.hasNext()){
			HashMap<String,String> h = it.next();
			assertEquals( h.get("0"),privacy.applyPrivacy(field, h.get("raw"), 0) );
			assertEquals(  h.get("1"),privacy.applyPrivacy(field, h.get("raw"), 1));
		}
    }
    
    @Test
    public void test_applyPrivacy_address() throws ScriptException, IOException{
    	ApplyPrivacySettingsJS privacy = new ApplyPrivacySettingsJS();
		ArrayList<HashMap<String,String>> inputDatas = addressTestDataGenerator();
		Iterator<HashMap<String,String>> it =inputDatas.iterator();
		
		while ( it.hasNext()){
			HashMap<String,String> h = it.next();
			String field="address";
		
			assertEquals( h.get("0"),privacy.applyPrivacy(field, h.get("raw"), 0) );
			assertEquals(  h.get("1"),privacy.applyPrivacy(field, h.get("raw"), 1));
			assertEquals( h.get("2"), privacy.applyPrivacy(field, h.get("raw"), 2));
			assertEquals( h.get("3"),privacy.applyPrivacy(field, h.get("raw"), 3));
			assertEquals( h.get("4"),privacy.applyPrivacy(field, h.get("raw"), 4));
			assertEquals( h.get("5"),privacy.applyPrivacy(field, h.get("raw"), 5));
			
		}
    }
    
    @Test
    public void test_applyPrivacysetings
     
    @Override
    protected RouteBuilder createRouteBuilder() {
        return new APIService();
    }
}