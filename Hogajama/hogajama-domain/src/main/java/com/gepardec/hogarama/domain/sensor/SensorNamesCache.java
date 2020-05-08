package com.gepardec.hogarama.domain.sensor;

import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import org.slf4j.Logger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Startup
@Singleton
public class SensorNamesCache {

	private static final int TEN_SECONDS = 10000;

    @Inject
	private Logger log;
	
	@Inject
	private SensorDataDAO habaramaDao;
	
	private List<String> sensorNames = new ArrayList<>();

    private long cacheTime = 0;
	
	public synchronized void loadSensorsFromDB() {
        if(isStale()) {
            log.info("Load the sensorNames from the database");
            sensorNames.clear();
            sensorNames = habaramaDao.getAllSensors();
            cacheTime = (new Date()).getTime();
        }
	}
	
	public List<String> getSensorNames() {
		if(isStale()) {
			loadSensorsFromDB();
		}
		return sensorNames;
	}

    private boolean isStale() {
        return sensorNames.isEmpty() || ((new Date()).getTime() - cacheTime) > TEN_SECONDS ;
    }

}
