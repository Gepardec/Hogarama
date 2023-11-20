package com.gepardec.hogarama.domain.sensor;

import org.gepardec.slog.SLogged;
import org.gepardec.slog.SLogger;
import org.gepardec.slog.level.SLogInfo;

import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Startup
@Singleton
@SLogged
public class SensorNamesCache {

	private static final int TEN_SECONDS = 10000;

    @Inject
    private SLogger logger;
	
	@Inject
	private SensorDataDAO habaramaDao;
	
	private List<String> sensorNames = new ArrayList<>();

    private long cacheTime = 0;
	
	public synchronized void loadSensorsFromDB() {
	    
	    SensorNamesCacheLog log = logger.add(new SensorNamesCacheLog());
	    
        if(isStale()) {
            log.setLoadSensorNames(true);
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
    
    @SLogInfo
    private class SensorNamesCacheLog {
        private boolean loadSensorNames;

        public boolean isLoadSensorNames() {
            return loadSensorNames;
        }

        public void setLoadSensorNames(boolean loadSensorNames) {
            this.loadSensorNames = loadSensorNames;
        }
    }
}
