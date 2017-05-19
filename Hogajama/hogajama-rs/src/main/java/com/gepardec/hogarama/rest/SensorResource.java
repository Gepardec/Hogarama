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
	@Path("allData/limit={maxNumber}")
	@Produces("application/json")
	public String getAllData( @PathParam("maxNumber") int maxNumber ) {
		return habaramaDAO.getAllSensorData(maxNumber);
	}
	
	@GET
	@Path("allData/")
	@Produces("application/json")
	public String getAllData( ) {
		return habaramaDAO.getAllSensorData();
	}	
	
	@GET
	@Path("/")
	@Produces("application/json")
	public String getAllSensors( ) {
		return habaramaDAO.getAllSensors();
	}	
	
	@GET
	@Path("allData/sensor={sensorName}")
	@Produces("application/json")
	public String getDataBySensorName(@PathParam("sensorName") String sensorName) {
		return habaramaDAO.getDataBySensorName(sensorName, -1);
	}
	
	@GET
	@Path("allData/sensor={sensorName}/limit={maxNumber}")
	@Produces("application/json")
	public String getDataBySensorName(@PathParam("sensorName") String sensorName, @PathParam("maxNumber") int maxNumber) {
		return habaramaDAO.getDataBySensorName(sensorName, maxNumber);
	}
	
}
