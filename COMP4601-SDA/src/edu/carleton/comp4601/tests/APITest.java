package edu.carleton.comp4601.tests;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import edu.carleton.comp4601.dao.Document;

public class APITest {

	private static String REST = "rest";
	private static String SDA = "sda";
	private static String BASE_URL = "http://localhost:8080/COMP4601-SDA";

	public static void main(String[] args) {
		System.out.println("API test");

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());
		
		
		// add document
		Document doc = new Document();
		doc.setId(new Integer(1));
		doc.setName("TEST");
		doc.setText("Hello world!");
		doc.addTag("world");
		doc.addTag("hello");
		doc.setUrl("https://localhost:8080/test");
		doc.addLink("https://localhost:8080/world");
		doc.addLink("https://localhost:8080/hello");
		
		ClientResponse response = service.path(REST).path(SDA)
				.accept(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, doc);
		
		System.out.println("Add Document Response: " + response.toString());
		
		
		// add document
		Document doc3 = new Document();
		doc3.setId(new Integer(2));
		doc3.setName("TEST");
		doc3.setText("Hello world!");
		doc3.addTag("test");
		doc3.setUrl("https://localhost:8080/test");
		doc3.addLink("https://localhost:8080/world");
		doc3.addLink("https://localhost:8080/hello");
		
		response = service.path(REST).path(SDA)
				.accept(MediaType.APPLICATION_XML)
				.post(ClientResponse.class, doc3);
		
		System.out.println("Add Document Response: " + response.toString());
		
		
		// test get document
		response = service.path(REST).path(SDA).path("/" + 1)
				.accept(MediaType.APPLICATION_XML)
				.get(ClientResponse.class);
		
		System.out.println("Get document response: " + response);
		
		
		//test update document
		Document doc2 = new Document();
		doc2.setId(doc.getId());
		doc2.addTag("update");
		doc2.addTag("test");
		doc2.addLink("https://localhost:8080/update");
		doc2.addLink("https://localhost:8080/test");
		response = service.path(REST).path(SDA).path("/" + 1)
				.accept(MediaType.APPLICATION_XML)
				.post(ClientResponse.class,doc2);
		
		System.out.println("Get updated user: " + response);
		
		/*test delete document
		response = service.path(REST).path(SDA).path("/" + 1)
				.accept(MediaType.APPLICATION_XML)
				.delete(ClientResponse.class);
		
		System.out.println("Deleting user: " + response);*/
		
		/*test delete document
				response = service.path(REST).path(SDA).path("delete" + "/update,test")
						.accept(MediaType.APPLICATION_XML)
						.get(ClientResponse.class);
				
				System.out.println("Deleting user: " + response);*/
		
		
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(BASE_URL).build();
	}

}
