package com.gepardec.hogarama.domain.watering;

import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.gepardec.slog.SLogged;
import org.gepardec.slog.SLogger;
import org.gepardec.slog.level.SLogInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.sensor.SensorData;

@ApplicationScoped
public class WateringStrategy {
    
    @Inject
    private SLogger logger;

 
    // Application scoped cache is not ideal, for now its good enough.
    private Map<String, Double> cache = new HashMap<String, Double>();


    private WateringStrategyLog log;
	
	public WateringStrategy() {
	}
	
    public WateringStrategy(SLogger logger) {
        this.logger = logger;
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("Initialized WateringStrategy");        
    }
    
    private int waterDuration(WateringRule config, double avg) {
        log.setConfig(config);
         log.setMoistureValue(avg);
        
        if ( avg < config.getLowWater() ) {
            log.setWater(true);
            int dur = config.getWaterDuration();
            log.setDuration(dur);
			return dur;
		}
        log.setWater(false);
        log.setDuration(0);
        return 0;
    }


    @SLogged
    public int computeWateringDuration(WateringRule config, double value) {
        log = logger.add(new WateringStrategyLog());
        
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
    
    @SLogInfo
    public class WateringStrategyLog {
        private boolean water;
        private double duration;
        private WateringRule config;
        private double moistureValue;
        
        public boolean isWater() {
            return water;
        }
        public void setWater(boolean water) {
            this.water = water;
        }
        public double getDuration() {
            return duration;
        }
        public void setDuration(double duration) {
            this.duration = duration;
        }
        public WateringRule getConfig() {
            return config;
        }
        public void setConfig(WateringRule config) {
            this.config = config;
        }
        public double getMoistureValue() {
            return moistureValue;
        }
        public void setMoistureValue(double moistureValue) {
            this.moistureValue = moistureValue;
        }        
    }
	
}
