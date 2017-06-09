package com.gepardec.hogarama.rest;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.gepardec.hogarama.rest.mapper.SensorMapper;
import com.gepardec.hogarama.service.dao.HabaramaDAO;

@Path("sensor")
public class SensorApiImpl implements SensorApi {
	
	@Context
	private UriInfo context;
	
	@Inject
	private HabaramaDAO habaramaDAO;

	@Override
	public Response getAllDataMaxNumber(Integer maxNumber, String sensor, SecurityContext securityContext) {
		List<SensorData> sensorData = SensorMapper.INSTANCE.mapSensors(habaramaDAO.getAllData(maxNumber, sensor));
		return Response.ok(sensorData).build();
	}

	@Override
	public Response getAllSensors(SecurityContext securityContext) {
		return Response.ok(habaramaDAO.getAllSensors()).build();
	}
	
}
