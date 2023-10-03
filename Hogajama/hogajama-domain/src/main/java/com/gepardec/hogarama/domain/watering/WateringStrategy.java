package com.gepardec.hogarama.domain.watering;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WateringStrategy {
    private static final Logger log = LoggerFactory.getLogger(WateringStrategy.class);
 
    // Application scoped cache is not ideal, for now its good enough.
    private Map<String, Double> cache = new HashMap<String, Double>();
	
	public WateringStrategy() {
	    log.info("Initializing WateringStrategy");
	}
	
    private int waterDuration(WateringRule config, double avg) {
        if ( avg < config.getLowWater() ) {
            int dur = config.getWaterDuration();
            log.info("water " + config.getActorName() + " for " + dur + " because average water value " + avg + " < " + config.getLowWater());
			return dur;
		}
        log.debug("Don't water " + config.getActorName() + " because average water value " + avg + " not < " + config.getLowWater());
        return 0;
    }


    public int computeWateringDuration(WateringRule config, double value) {
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


    private Double getCachedValue(WateringRule config) {
        return cache.get(config.getSensorName());
    }
    private void updateCache(WateringRule config, double avg) {
        cache.put(config.getSensorName(), avg);
        
    }
	
}
