package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.sensor.SensorNamesCache;
import com.gepardec.hogarama.domain.watering.WateringDAO;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.rest.mapper.SensorMapper;
import com.gepardec.hogarama.rest.util.DateUtil;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Path("sensor")
@SessionScoped
public class SensorApiImpl implements SensorApi, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SensorDataDAO habaramaDAO;

	@Inject
	private WateringDAO wateringDAO;

	@Inject
	private SensorNamesCache sensorCache;

	@Override
	public Response getAllSensors(SecurityContext securityContext) {
		return Response.ok(sensorCache.getSensors()).build();
	}
	

	@Override
	public Response getAllDataMaxNumber(Boolean onlyDataFromToday, Integer maxNumber, String sensor, String from, String to,
			SecurityContext securityContext) {
		/*
		 * TODO: From and To as Date. See in swagger conf. @DateTimeParam refactor Date
		 * format swagger: 2018-02-19T13:46:01.149Z yyyy-MM-ddTHH:mm:ss.SSSZ
		 */
		Date fromDate = DateUtil.getDateTimeFromString(from);
		Date toDate = DateUtil.getDateTimeFromString(to);

		if (onlyDataFromToday) {
			fromDate = getTodayStartTime();
			toDate = getTodayEndTime();
		}
		List<SensorData> sensorData = SensorMapper.INSTANCE.mapSensors(habaramaDAO.getAllData(maxNumber, sensor, fromDate, toDate));
		return Response.ok(sensorData).build();
	}

	@Override
	public Response getAllWateringDataMaxNumber(Boolean onlyDataFromToday, Integer maxNumber, String sensor, String from, String to, SecurityContext securityContext) {
		Date fromDate = DateUtil.getDateTimeFromString(from);
		Date toDate = DateUtil.getDateTimeFromString(to);

		if (onlyDataFromToday) {
			fromDate = getTodayStartTime();
			toDate = getTodayEndTime();
		}
		List<WateringData> wateringData = wateringDAO.getWateringData(maxNumber, sensor, fromDate, toDate);
		return Response.ok(wateringData).build();
	}

	@Override
	public Response getLocationBySensorName(String sensorName, SecurityContext securityContext) {
		return Response.ok(sensorCache.getLocationBySensorName(sensorName)).build();
	}

	private Date getTodayStartTime() {
		return DateUtil.getTime(0, 0, 0);
	}

	private Date getTodayEndTime() {
		return DateUtil.getTime(23, 59, 59);
	}

}
