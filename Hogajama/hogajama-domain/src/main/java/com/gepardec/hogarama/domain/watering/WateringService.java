package com.gepardec.hogarama.domain.watering;

import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;

import javax.inject.Inject;
import java.time.LocalDateTime;

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
	public SensorDataDAO sensorDataDao;

	@Inject
	public WateringConfigDAO configDao;

	@Inject
	public ActorService actor;

	@Inject
	public WateringStrategy watering;

	private LocalDateTime now;

	public WateringService() {
	}

	protected WateringService(SensorDataDAO sensorDataDao, ActorService actor, WateringStrategy watering, WateringConfigDAO configDao) {
		this.sensorDataDao = sensorDataDao;
		this.actor = actor;
		this.watering = watering;
		this.configDao = configDao;
	}

	public void waterAll() {
		for (String sensorName : sensorDataDao.getAllSensors()) {
			int dur = watering.water(getConfig(sensorName), getDate());
			if (dur > 0) {
				Metrics.wateringEventsFired.labels(sensorName).inc();
				actor.sendActorMessage(sensorDataDao.getLocationBySensorName(sensorName), sensorName, dur);
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
