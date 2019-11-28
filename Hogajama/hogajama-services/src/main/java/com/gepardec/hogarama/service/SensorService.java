package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Sensor;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.sensor.SensorDao;

import javax.inject.Inject;
import java.util.List;

public class SensorService {

    @Inject
    private SensorDao dao;
    @Inject
    private OwnerStore store;

    public List<Sensor> getAllSensors() {
        return dao.findAll();
    }

    public List<Sensor> getAllSensorForOwner() {
        return dao.getAllSensorForOwner(store.getOwner().getId());
    }

    public void createSensor(Sensor sensor) {
        if (store.getOwner().getUnitList().contains(sensor.getUnit())) {
            dao.save(sensor);
        } else {
            throw new TechnicalException("Unit doesn't belong to owner");
        }
    }

    public void createSensorForDefaultUnit(Sensor sensor) {
        sensor.setUnit(store.getOwner().getDefaultUnit());
        createSensor(sensor);
    }

}
