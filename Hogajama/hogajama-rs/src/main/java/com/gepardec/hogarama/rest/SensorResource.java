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
	@Path("allData/{maxNumber}")
	@Produces("application/json")
	public String getMongoDb( @PathParam("maxNumber") int maxNumber ) {
		String habarama = habaramaDAO.getAllSensorData(maxNumber);
		return habarama;
	}
	
	@GET
	@Path("allData/")
	@Produces("application/json")
	public String getMongoDb( ) {
		String habarama = habaramaDAO.getAllSensorData();
		return habarama;
	}	
	
	@GET
	@Path("/")
	@Produces("application/json")
	public String getAllSensors( ) {
		String habarama = habaramaDAO.getAllSensors();
		return habarama;
	}	
	
}
