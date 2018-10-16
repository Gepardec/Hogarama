package com.gepardec.hogarama.domain.watering;

import java.time.LocalDateTime;

import javax.inject.Inject;

import com.gepardec.hogarama.domain.sensor.SensorDAO;

public class WateringService {

	public enum Config{

		DEFAULT(1, 0.2, 5);
		
		protected int measureInterval;
		protected double lowWater;
		protected int waterDuration;
		
		Config( int measureInterval, double lowWater, int waterDuration) {
			this.waterDuration = waterDuration;
			this.measureInterval = measureInterval;
			this.lowWater = lowWater;
		}
	}

	@Inject
	public SensorDAO sensorDao;

	@Inject
	public WateringConfigDAO configDao;

	@Inject
	public ActorService actor;

	@Inject
	public WateringStrategy watering;

	private LocalDateTime now;

	public WateringService() {
	}

	protected WateringService(SensorDAO sensorDao, ActorService actor, WateringStrategy watering, WateringConfigDAO configDao) {
		this.sensorDao = sensorDao;
		this.actor = actor;
		this.watering = watering;
		this.configDao = configDao;
	}

	public void waterAll() {
		for (String sensorName : sensorDao.getAllSensors()) {
			int dur = watering.water(getConfig(sensorName), getDate());
			if (dur > 0) {
				actor.sendActorMessage(sensorDao.getLocationBySensorName(sensorName), sensorName, dur);
			}
		}
	}

	private WateringConfigData getConfig(String sensorName) {
		WateringConfigData wconfig = configDao.getBySensorName(sensorName);
		if ( null != wconfig ) {
			return wconfig;
		}
		
		wconfig = new WateringConfigData(sensorName, sensorName, Config.DEFAULT.measureInterval,
				Config.DEFAULT.lowWater, Config.DEFAULT.waterDuration);
		configDao.save(wconfig);
		return wconfig;
	}

	protected void setDate(LocalDateTime now) {
		this.now = now;
	}

	private LocalDateTime getDate() {
		if (null != now) {
			return now;
		}
		return LocalDateTime.now();
	}

}