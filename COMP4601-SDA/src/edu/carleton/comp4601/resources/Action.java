package edu.carleton.comp4601.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import edu.carleton.comp4601.dao.Document;
import edu.carleton.comp4601.dao.DocumentCollection;

public class Action {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	String id;

	public Action(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Document getDocumentHTML() {
		DocumentCollection dc = new DocumentCollection();
		List<Document> dl = dc.getDocuments();
		
		for(Document d: dl){
			if(d.getId() == new Integer(id)){
				return d;
			}
		}
		
		throw new RuntimeException("No such doc with id:" + id);	
	}

	

	
}