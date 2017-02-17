package com.gepardec.hogarama.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.gepardec.hogarama.domain.Habarama;
import com.gepardec.hogarama.service.dao.HabaramaDAO;

/**
 * Root resource (exposed at "helloworld" path)
 */
@Path("helloworld")
public class HelloWorld {
	@Context
	private UriInfo context;
	
	@Inject
	private HabaramaDAO habaramaDAO;
	
	/** Creates a new instance of HelloWorld */
	public HelloWorld() {
	}

	/**
	 * Retrieves representation of an instance of helloWorld.HelloWorld
	 * 
	 * @return an instance of java.lang.String
	 */
	@GET
	@Produces("text/html")
	public String getHtml() {
		return "<html lang=\"en\"><body><h1>Hogarama!</h1></body></html>";
	}
	
	@GET
	@Path("mongodb")
	@Produces("text/html")
	public String getMongoDb() {
		List<Habarama> habarama = habaramaDAO.query();
		return habarama.toString();
	}
}