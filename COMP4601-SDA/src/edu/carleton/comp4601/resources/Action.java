package edu.carleton.comp4601.resources;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.dao.Document;
import edu.carleton.comp4601.dao.Documents;

public class Action {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	Integer id;
	
	Documents documents;

	public Action(UriInfo uriInfo, Request request, Integer id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
		
		documents = Documents.getInstance();
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Document getDocumentXML() {
		Document doc = documents.get(id);
		
		if(doc != null){
			return doc;
		}
		
		throw new RuntimeException("No such doc with id:" + id);
	}

	@POST
	//@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_XML)
	public String updateUser(JAXBElement<Document> d, @Context HttpServletResponse servletResponse)
			throws IOException {
		Document doc = d.getValue();
		if (documents.update(id, doc)) {
			servletResponse.setStatus(200);
			return "UPDATED DOCUMENT";
		}
		
		throw new RuntimeException("No such doc with id:" + id);
	}
	
	@DELETE
	public String deleteDocument(@Context HttpServletResponse servletResponse) {
		if (!Documents.getInstance().remove(id))
			throw new RuntimeException("Account " + id + " not found");
		servletResponse.setStatus(200);
		return "REMOVED DOCUMENT";
	}
	
}