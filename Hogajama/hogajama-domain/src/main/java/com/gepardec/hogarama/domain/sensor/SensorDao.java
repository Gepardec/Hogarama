package com.gepardec.hogarama.domain.sensor;

import com.gepardec.hogarama.domain.GenericDao;
import com.gepardec.hogarama.domain.entity.Sensor;

import java.util.List;

public interface SensorDao extends GenericDao<Sensor> {

    List<Sensor> getAllSensorForOwner(Long ownerId);
}
