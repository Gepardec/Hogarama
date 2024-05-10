package com.gepardec.hogarama.domain.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class SensorNamesCache {

	private static final int TEN_SECONDS = 10000;

    private static final Logger log = LoggerFactory.getLogger(SensorNamesCache.class);
	
	@Inject
	private SensorDAO sensorDAO;
	
    @Inject
    private UserContext userContext;
	
	private List<Sensor> sensors;

    private long cacheTime = 0;
	
	private synchronized void loadSensorsFromDB() {
        if(isStale()) {
            log.info("Load the sensorNames from the database");
            sensors.clear();
            sensors = sensorDAO.getAllSensorsForUser(userContext.getUser());
            sensors.stream().forEach(s -> s.getUnit()); // lazyload Units
            cacheTime = (new Date()).getTime();
        }
	}
	
	public List<String> getSensors() {
		if(isStale()) {
			loadSensorsFromDB();
		}
		return sensors.stream()
                .map(s -> s.getName())
                .collect(Collectors.toList());
	}

    private boolean isStale() {
        return sensors.isEmpty() || ((new Date()).getTime() - cacheTime) > TEN_SECONDS ;
    }

    public String getLocationBySensorName(String sensorName) {
        return getSensor(sensorName).get().getUnit().getName();
    }

    private Optional<Sensor> getSensor(String sensorName) {
        return sensors.stream().filter(s -> s.getName().equals(sensorName)).findFirst();
    }

}
