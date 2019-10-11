package com.gepardec.hogarama.domain.sensor;

import com.gepardec.hogarama.domain.metrics.Metrics;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gepardec.hogarama.domain.DateUtils.toDate;

@ApplicationScoped
public class SensorMetrics {

    @Inject
    private SensorDAO sensorDao;
    

    public void collect() {

        Map<String, SensorData> values = getLatestValues();
        for (String key : values.keySet()) {
            SensorData sensorData = values.get(key);
            Metrics.sensorValues.labels(sensorData.getSensorName(), sensorData.getLocation()).set(sensorData.getValue());
        }
    }

    private Map<String, SensorData> getLatestValues() {
        LocalDateTime now = LocalDateTime.now();
        return getLatestValues(sensorDao.getAllData(200, SensorDAO.ALL_SENSORS,
                toDate(now.minus(Duration.ofMinutes(60))),
                toDate(now.plus(Duration.ofSeconds(1)))));
    }
    
    public static Map<String, SensorData> getLatestValues(List<SensorData> values) {
        Map<String, SensorData> latests = new HashMap<>();
        
        for (SensorData sensorData : values) {
            String name = sensorData.getSensorName();
            if ( null == latests.get(name) || sensorData.getTime().after(latests.get(name).getTime()) ) {
                latests.put(name, sensorData);
            }
        }
        return latests;
    }
}
