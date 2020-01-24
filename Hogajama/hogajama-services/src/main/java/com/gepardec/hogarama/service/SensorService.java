package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Sensor;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.service.dao.SensorDao;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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


    public void deleteSensor(Long sensorId) {
        Sensor sensor = this.dao.getById(sensorId)
                .orElseThrow(() -> new NotFoundException(String.format("Sensor with id [%d] not found", sensorId)));

        this.dao.delete(sensor);
    }

    public void updateSensor(Sensor sensor) {
        if (store.getOwner().getUnitList().stream().map(Unit::getId).collect(Collectors.toSet()).contains(sensor.getUnit().getId()) && sensorExists(sensor)) {
            dao.update(sensor);
        } else {
            throw new TechnicalException("Unit doesn't belong to owner");
        }
    }

    private boolean sensorExists(Sensor sensor) {
        return dao.getById(sensor.getId()).isPresent();
    }

    public void createSensor(Sensor sensor) {
        if (store.getOwner().getUnitList().stream().map(Unit::getId).collect(Collectors.toSet()).contains(sensor.getUnit().getId())) {
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
