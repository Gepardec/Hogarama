package com.gepardec.hogarama.rest;

import java.util.Date;
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
	public Response getAllSensors(SecurityContext securityContext) {
		return Response.ok(habaramaDAO.getAllSensors()).build();
	}

	@Override
	public Response getAllDataMaxNumber(Integer maxNumber, String sensor, Date from, Date to,
			SecurityContext securityContext) {
		List<SensorData> sensorData = SensorMapper.INSTANCE.mapSensors(habaramaDAO.getAllData(maxNumber, sensor, from, to));
		return Response.ok(sensorData).build();
	}
	
}
