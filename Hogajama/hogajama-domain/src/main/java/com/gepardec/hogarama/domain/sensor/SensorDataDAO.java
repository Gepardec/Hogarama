package com.gepardec.hogarama.domain.sensor;

import java.util.Date;
import java.util.List;

import com.gepardec.hogarama.domain.watering.WateringData;

public interface SensorDataDAO {

    List<SensorData> getAllData(Integer maxNumber, String sensorName, Date from, Date to);

    void save(SensorData sensorData);

    void saveActorEvent(WateringData data);
}
