package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class SensorService {

    @Inject
    private SensorDAO dao;
    @Inject
    private OwnerStore store;

    public void createSensor(Sensor sensor) {
        verifyUnitBelongsToOwner(sensor.getUnit());
        dao.save(sensor);
    }

    public void deleteSensor(Long sensorId) {
    }

    public void updateSensor(Sensor sensor) {

    }

    public List<Sensor> getAllSensorForOwner() {
        return dao.getAllSensorForOwner(store.getOwner().getId());
    }

    private void verifyUnitBelongsToOwner(Unit unit) {
        if (!unitBelongsToOwner(unit)) {
            throw new TechnicalException("Unit doesn't belong to owner");
        }
    }

    private boolean unitBelongsToOwner(Unit unit) {
        return store.getOwner().getUnitList().stream().map(Unit::getId).collect(Collectors.toSet()).contains(unit.getId());
    }
}
