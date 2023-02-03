package com.gepardec.hogarama.domain.watering;

import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.gepardec.hogarama.domain.sensor.SensorProperties;
import com.gepardec.hogarama.domain.unitmanagement.cache.ActorCache;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Optional;

public class WateringService {

	public enum Config{

		DEFAULT(1, 0.2, 5);
		
		protected double lowWater;
		protected int waterDuration;
		
		Config( int measureInterval, double lowWater, int waterDuration) {
			this.waterDuration = waterDuration;
			this.lowWater = lowWater;
		}
	}

	@Inject
	public SensorDataDAO sensorDataDAO;

	@Inject
	public WateringRuleDAO configDao;

	@Inject
	SensorNormalizer sensorNormalizer;

	@Inject
	public ActorControlService actorSvc;

	@Inject
	public WateringStrategy watering;

	@Inject
	private ActorCache actorCache;

	@Inject
	private SensorCache sensorCache;

    private static final Logger log = LoggerFactory.getLogger(WateringService.class);

	public WateringService() {
	}

	protected WateringService(SensorDataDAO sensorDataDAO, SensorNormalizer normalizer, ActorControlService actorSvc, WateringStrategy watering, WateringRuleDAO configDao, ActorCache actorCache, SensorCache sensorCache) {
		this.sensorDataDAO = sensorDataDAO;
		this.sensorNormalizer = normalizer;
		this.actorSvc = actorSvc;
		this.watering = watering;
		this.configDao = configDao;
		this.actorCache = actorCache;
		this.sensorCache = sensorCache;
	}

    private void invokeActorIfNeeded(WateringRule config, int dur, String location) {
        if (dur > 0) {

			Optional<Actor> optionalActor = actorCache.getByDeviceId(config.getActorName());
			String actorName = optionalActor.isPresent() ? optionalActor.get().getName() : config.getActorName();
			Optional<Sensor> optionalSensor = sensorCache.getByDeviceId(config.getSensorName());
			String sensorName = optionalSensor.isPresent() ? optionalSensor.get().getName() : config.getSensorName();

			Metrics.wateringEventsFired.labels(sensorName, actorName).inc();
			Metrics.actorValues.labels(
					actorName,
					sensorName,
					location
					).set(dur);

        	actorSvc.sendActorMessage(location, config.getActorName(), dur);
        }
    }

	private WateringRule getConfig(String sensorName) {
	    WateringRule wconfig = configDao.getBySensorName(sensorName);
		if (null != wconfig) {
			return wconfig;
		}

		wconfig = configDao.createWateringRule(sensorName, sensorName,
				Config.DEFAULT.lowWater, Config.DEFAULT.waterDuration);
		configDao.save(wconfig);
		return wconfig;
	}

	private void sendMetrics(SensorData sensorData) {
		SensorProperties sensorProps = new SensorProperties(sensorData, sensorCache);
		Metrics.sensorValues.labels(
				sensorProps.getSensorName(),
				sensorProps.getDeviceId(),
				sensorProps.getUnitName()
		).set(sensorData.getValue());

	}

	public void processSensorData(SensorData sensorData) {
		SensorData normalizedSensorData = sensorNormalizer.normalize(sensorData);
		sensorDataDAO.save(sensorData);
		sendMetrics(normalizedSensorData);

		checkDataAndPerformWatering(normalizedSensorData);
	}

	private void checkDataAndPerformWatering(SensorData normalizedSensorData) {
		WateringRule config = getConfig(normalizedSensorData.getSensorName());
		invokeActorIfNeeded(config, watering.computeWateringDuration(config, normalizedSensorData.getValue()), normalizedSensorData.getLocation());
	}

}
