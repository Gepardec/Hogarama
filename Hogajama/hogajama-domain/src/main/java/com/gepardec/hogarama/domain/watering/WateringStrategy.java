package com.gepardec.hogarama.domain.watering;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.gepardec.hogarama.domain.sensor.SensorDAO;

public class WateringStrategy {
 
	@Inject
	private SensorDAO sensorDao;
    private Map<String, Double> cache = new HashMap<String, Double>();
	
	public WateringStrategy() {
	}
	
	protected WateringStrategy(SensorDAO dao) {
		this.sensorDao = dao;
	}

    private int waterDuration(WateringConfigData config, double avg) {
        if ( avg < config.getLowWater() ) {
			return config.getWaterDuration();
		}
		else return 0;
    }


    public int computeWateringDuration(WateringConfigData config, double value) {
        double avg = computeAverage(getCachedValue(config), value);
        updateCache(config, avg);
        return waterDuration(config,avg);
    }

    private double computeAverage(Double cachedValue, double value) {
        if ( null == cachedValue ) {
            cachedValue = value;
        }
        return (cachedValue * 2 + value) / 3;
    }


    private Double getCachedValue(WateringConfigData config) {
        return cache.get(config.getSensorName());
    }
    private void updateCache(WateringConfigData config, double avg) {
        cache.put(config.getSensorName(), avg);
        
    }
	
}
