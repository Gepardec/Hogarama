package com.gepardec.hogarama.rest;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gepardec.hogarama.rest.mapper.SensorMapper;
import com.gepardec.hogarama.service.dao.HabaramaDAO;
import com.gepardec.hogarama.service.schedulers.SensorsScheduler;

@Path("sensor")
@SessionScoped
public class SensorApiImpl implements SensorApi, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HabaramaDAO habaramaDAO;
	
	@Inject
	private SensorsScheduler sensorsScheduler;

	@Override
	public Response getAllSensors(SecurityContext securityContext) {
		return Response.ok(sensorsScheduler.getSensorNames()).build();
	}

	@Override
	public Response getAllDataMaxNumber(Integer maxNumber, String sensor, Date from, Date to,
			SecurityContext securityContext) {
		List<SensorData> sensorData = SensorMapper.INSTANCE.mapSensors(habaramaDAO.getAllData(maxNumber, sensor, from, to));
		return Response.ok(sensorData).build();
	}
}
