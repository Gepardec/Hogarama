package com.gepardec.hogarama.domain.watering;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.metrics.Metrics;
import com.gepardec.hogarama.domain.sensor.SensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;

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
	public ActorService actorSvc;

	@Inject
	public WateringStrategy watering;

    private static final Logger log = LoggerFactory.getLogger(WateringService.class);

	public WateringService() {
	}

	protected WateringService(SensorDAO sensorDao, ActorService actorSvc, WateringStrategy watering, WateringConfigDAO configDao) {
		this.sensorDao = sensorDao;
		this.actorSvc = actorSvc;
		this.watering = watering;
		this.configDao = configDao;
	}

    private void invokeActorIfNeeded(WateringConfigData config, int dur) {
        if (dur > 0) {
        	Metrics.wateringEventsFired.labels(config.getSensorName()).inc();
        	log.info("water " + config.getActorName() + " for " + dur);
        	actorSvc.sendActorMessage(sensorDao.getLocationBySensorName(config.getSensorName()), config.getActorName(), dur);
        }
        else {
            log.debug("Don't water " + config.getSensorName());            
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

    public void water(SensorData sensorData) {
        WateringConfigData config = getConfig(sensorData.getSensorName());
        invokeActorIfNeeded(config, watering.computeWateringDuration(config, sensorData.getValue()));
    }

}