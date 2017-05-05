package com.gepardec.hogarama.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.gepardec.hogarama.service.dao.HabaramaDAO;

@Path("sensor")
public class SensorResource {
	
	@Context
	private UriInfo context;
	
	@Inject
	private HabaramaDAO habaramaDAO;
	
	@GET
	@Path("mongodb/{maxNumber}")
	@Produces("application/json")
	public String getMongoDb( @PathParam("maxNumber") int maxNumber ) {
		String habarama = habaramaDAO.queryDataFromMongoDb(maxNumber);
		return habarama;
	}
	
	@GET
	@Path("mongodb/")
	@Produces("text/html")
	public String getMongoDb( ) {
		String habarama = habaramaDAO.queryDataFromMongoDb();
		return habarama;
	}	
	
	@GET
	@Path("cassandra/")
	@Produces("application/json")
	public String getCassandra() {
		System.out.println("Cassandra.getCassandra");
		return habaramaDAO.queryDataFromCassandra();
	}
	
	@GET
	@Path("cassandra/{maxNumber}")
	@Produces("application/json")
	public String getCassandra(@PathParam("maxNumber") int maxNumber ) {
		System.out.println("Cassandra.getCassandra(parameter)");
		return habaramaDAO.queryDataFromCassandra(maxNumber);
	}

}
