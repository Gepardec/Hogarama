package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
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
    private UserContext userContext;

    @Inject
    Event<Sensor> sensorChanged;
    
    public void createSensor(Sensor sensor) {
        sensor.verifyIsOwned(userContext.getOwner());
        dao.save(sensor);
    }

    public void deleteSensor(Long sensorId) {
        Sensor sensor = this.dao.getById(sensorId)
                .orElseThrow(() -> new NotFoundException(String.format("Sensor with id [%d] not found", sensorId)));

        sensorChanged.fire(sensor);
        dao.delete(sensor);
    }

    public void updateSensor(Sensor sensor) {
        sensor.verifyIsOwned(userContext.getOwner());
        sensorChanged.fire(sensor);
        dao.update(sensor);
    }

    public List<Sensor> getAllSensorsForOwner() {
        return dao.getAllSensorsForOwner(userContext.getOwner());
    }

}
