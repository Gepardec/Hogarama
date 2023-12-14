package com.gepardec.hogarama.domain.unitmanagement.service;

import java.util.List;

import org.gepardec.slog.SLogged;
import org.gepardec.slog.SLogger;
import org.gepardec.slog.SLoggerProducer;
import org.gepardec.slog.level.SLogInfo;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.UserDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;

@SLogged
@Stateless
public class SensorService {
    
    @Inject
    private SLogger logger;

    @Inject
    private SensorDAO dao;

    @Inject
    private UserDAO userDao;

    @Inject
    private UserContext userContext;

    @Inject
    Event<Sensor> sensorChanged;
    
    public void createSensor(Sensor sensor) {
        sensor.verifyIsOwned(userContext.getUser());
        dao.save(sensor);
    }

    public void deleteSensor(Long sensorId) {
        Sensor sensor = this.dao.getById(sensorId)
                .orElseThrow(() -> new NotFoundException(String.format("Sensor with id [%d] not found", sensorId)));

        sensorChanged.fire(sensor);
        dao.delete(sensor);
    }

    public void updateSensor(Sensor sensor) {
        sensor.verifyIsOwned(userContext.getUser());
        sensorChanged.fire(sensor);
        dao.update(sensor);
    }

    public List<Sensor> getAllSensorsForUser() {
        SensorServiceLog log = logger().add(new SensorServiceLog());
        log.setFunction("Hole Alle Sensoren");
        User user = userContext.getUser();
        log.setUser(user);
        
        List<Sensor> sensors = dao.getAllSensorsForUser(userContext.getUser());
        return sensors;
    }
    
    private SLogger logger() {
        if (null == logger) {
            return new SLoggerProducer().produceLogSystem(getClass());
        }
        return logger;
    }

    @SLogInfo
    private class SensorServiceLog {
        
        private String function;
        private User user;

        public String getFunction() {
            return function;
        }

        public void setFunction(String message) {
            this.function = message;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

    }

}
