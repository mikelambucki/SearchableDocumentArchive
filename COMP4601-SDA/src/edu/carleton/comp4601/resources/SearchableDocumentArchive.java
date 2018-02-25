package edu.carleton.comp4601.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;



@Path("/sda")
public class SearchableDocumentArchive {
		// Allows to insert contextual objects into the class,
		// e.g. ServletContext, Request, Response, UriInfo
		@Context
		UriInfo uriInfo;
		@Context
		Request request;

		private String name;

		public SearchableDocumentArchive() {
			name = "COMP4601 Searchable Document Archive V2.1: Michael Lambucki and Cole Traynor";
			
		}

		@GET
		public String printName() {
			return name;
		}

		@GET
		@Produces(MediaType.TEXT_XML)
		public String sayXML() {
			return "<?xml version=\"1.0\"?>" + "<sda> " + name + " </sda>";
		}

		@GET
		@Produces(MediaType.TEXT_HTML)
		public String sayHtml() {
			return "<html> " + "<title>" + name + "</title>" + "<body><h1>" + name
					+ "</body></h1>" + "</html> ";
		}

		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public String sayJSON() {
			return "{" + name + "}";
		}
}
