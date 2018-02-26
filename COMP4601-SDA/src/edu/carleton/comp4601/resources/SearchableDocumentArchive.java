package edu.carleton.comp4601.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;


import edu.carleton.comp4601.dao.Document;
import edu.carleton.comp4601.dao.Documents;
import edu.carleton.comp4601.utility.SDAConstants;
import edu.carleton.comp4601.utility.SearchResult;
import edu.carleton.comp4601.utility.SearchServiceManager;

@Path("/sda")
public class SearchableDocumentArchive {
		// Allows to insert contextual objects into the class,
		// e.g. ServletContext, Request, Response, UriInfo
		@Context
		UriInfo uriInfo;
		@Context
		Request request;

		private String name;
		
		//DocumentCollection dc = new DocumentCollection();

		public SearchableDocumentArchive() {
			name = "COMP4601 Searchable Document Archive V2.1: Michael Lambucki and Cole Traynor";
		}
		
		@GET
		public String printName() {
			return name;
		}
		
		@GET
		@Path("documents")
		@Produces(MediaType.APPLICATION_XML)
		public List<Document> getAccounts() {
			List<Document> loa = new ArrayList<Document>();
			loa.addAll(Documents.getInstance().getValues());
			return loa;
		}

		@GET	
		@Path("search/{tags}")	
		@Produces(MediaType.TEXT_HTML)	
		public String searchForDocs(@PathParam("tags")	String	tags)	{	
			//Perform	the	distributed	part	of	the	search	
			SearchResult sr	=	SearchServiceManager.getInstance().query(tags);	
			//Perform	your	local	search	(this	is	my	specific	code,	yours	differs!)	
			ArrayList<Document>	docs = Documents.getInstance().query(tags);	
			//We	will	wait	for	up	to	10	seconds	but	will	then	
			//	take	the	documents	that	we	have.	
			try	{	
			 	sr.await(SDAConstants.TIMEOUT,	TimeUnit.SECONDS);	
				}	catch	(InterruptedException	e)	{	
			}	
			//Take	the	state	of	the	documents	
			docs.addAll(sr.getDocs());	
			//Build	the	page	(not	provided	here)	
			return	documentsAsString(docs,	tags);
		}
		
		
		//Creates document
		@POST
		@Consumes(MediaType.APPLICATION_XML)
		public String newDocument(
				JAXBElement<Document> d,
				@Context HttpServletResponse servletResponse) throws IOException {
			System.out.println("Post request");
			Document doc = d.getValue();
			if (Documents.getInstance().add(doc)) {
				servletResponse.setStatus(200);
				return "ADDED DOCUMENT";
			}
			servletResponse.setStatus(204);
			return "COULD NOT DOCUMENT";
		}
		
		//Delete documents with specified tags
		@GET
		@Path("delete/{tags}")
		public String deleteDocuments(@PathParam("tags") String tags,@Context HttpServletResponse servletResponse) {
			
			System.out.println("DELETE MANY: " + tags);
			if (!Documents.getInstance().remove(tags)){
				servletResponse.setStatus(204);
				return "DOCUMENT WITH TAGS NOT FOUND";
			}
			servletResponse.setStatus(200);
			return "REMOVED DOCUMENTS";
		}
		
		//Get specific document
		@Path("{docId}")
		public Action getUser(@PathParam("docId") Integer id){
			return new Action(uriInfo, request, id);
		}
		
}
