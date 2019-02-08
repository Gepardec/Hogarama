package com.gepardec.hogarama.domain.sensor;

import static com.gepardec.hogarama.domain.DateUtils.toDate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.prometheus.client.Gauge;

@ApplicationScoped
public class SensorMetrics {

    @Inject
    private SensorDAO sensorDao;
    
    private Gauge sensorValues;
    
    public void collect() {

        Map<String, SensorData> values = getLatestValues();
        for (String key : values.keySet()) {
            SensorData sensorData = values.get(key);
            sensorValues.labels(sensorData.getSensorName()).set(sensorData.getValue());
        }
    }


    @PostConstruct
    private void createMetrics() {
        sensorValues = Gauge.build()
                .name("hogarama_sensor_value")
                .help("Water Sensor Values.").labelNames("sensor_name").register();
    }

    private Map<String, SensorData> getLatestValues() {
        LocalDateTime now = LocalDateTime.now();
        Map<String, SensorData> values = getLatestValues(sensorDao.getAllData(200, SensorDAO.ALL_SENSORS, 
                toDate(now.minus(Duration.ofMinutes(60))),
                toDate(now.plus(Duration.ofSeconds(1)))));
        return values;
    }
    
    static public Map<String, SensorData> getLatestValues(List<SensorData> values) {
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
