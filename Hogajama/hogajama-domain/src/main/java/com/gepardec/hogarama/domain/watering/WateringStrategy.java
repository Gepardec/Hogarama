package com.gepardec.hogarama.domain.watering;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.sensor.SensorDataDAO;

public class WateringStrategy {
    private static final Logger log = LoggerFactory.getLogger(WateringStrategy.class);
 
    private Map<String, Double> cache = new HashMap<String, Double>();
	
	public WateringStrategy() {
	}
	
    private int waterDuration(WateringConfigData config, double avg) {
        if ( avg < config.getLowWater() ) {
            int dur = config.getWaterDuration();
            log.info("water " + config.getActorName() + " for " + dur + " because water value " + avg + " < " + config.getLowWater());
			return dur;
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
