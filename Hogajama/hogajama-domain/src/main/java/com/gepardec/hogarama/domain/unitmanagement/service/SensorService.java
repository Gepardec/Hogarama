package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UnitManagementContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class SensorService {

    @Inject
    private SensorDAO dao;

    @Inject
    private UnitManagementContext store;

    @Inject
    Event<Sensor> sensorChanged;
    
    public void createSensor(Sensor sensor) {
        verifyUnitBelongsToOwner(sensor.getUnit());
        dao.save(sensor);
    }

    public void deleteSensor(Long sensorId) {
        Sensor sensor = this.dao.getById(sensorId)
                .orElseThrow(() -> new NotFoundException(String.format("Sensor with id [%d] not found", sensorId)));

        sensorChanged.fire(sensor);
        dao.delete(sensor);
    }

    public void updateSensor(Sensor sensor) {
        verifyUnitBelongsToOwner(sensor.getUnit());
        sensorChanged.fire(sensor);
        dao.update(sensor);
    }

    public List<Sensor> getAllSensorForOwner() {
        return dao.getAllSensorForOwner(store.getOwner());
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
