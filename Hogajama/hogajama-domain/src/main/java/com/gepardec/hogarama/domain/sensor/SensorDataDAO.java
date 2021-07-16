package com.gepardec.hogarama.domain.sensor;

import java.util.Date;
import java.util.List;

public interface SensorDataDAO {

    List<String> getAllSensors();
    List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to);
    String getLocationBySensorName(String sensorName);

    void save(SensorData sensorData);
}
