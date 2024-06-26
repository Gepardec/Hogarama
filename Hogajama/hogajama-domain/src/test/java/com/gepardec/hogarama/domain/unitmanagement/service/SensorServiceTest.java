package com.gepardec.hogarama.domain.unitmanagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import jakarta.enterprise.event.Event;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    private static final Long SENSOR_ID = 3537L;

    @Mock
    private SensorDAO dao;
 
    @Mock
    private UserContext userContext;

    @Mock
    Event<Sensor> sensorChanged;

    @InjectMocks
    private SensorService service;

    private Unit unit;
    private User user;

    @BeforeEach
    public void setUp() {
        user = newUser();
        Mockito.lenient().when(userContext.getUser()).thenReturn(user);
    }

    @Test
    public void createSensor_OK() {
        Sensor sensor = newSensor();

        service.createSensor(sensor);

        Mockito.verify(dao).save(sensor);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void createSensor_UnitDoesntBelongToUser() {
        assertThrows(TechnicalException.class, () -> {
            Sensor sensor = newSensorWithNotBelongingUnit();

            service.createSensor(sensor);
        });
    }

    @Test
    public void deleteSensor() {
        Sensor sensor = newSensor();
        Mockito.when(dao.getById(SENSOR_ID)).thenReturn(Optional.of(sensor));

        service.deleteSensor(SENSOR_ID);

        Mockito.verify(dao).delete(sensor);
        Mockito.verify(sensorChanged).fire(sensor);
    }

    @Test
    public void updateSensor_OK() {
        Sensor sensor = newSensor();

        service.updateSensor(sensor);

        Mockito.verify(dao).update(sensor);
        Mockito.verifyNoMoreInteractions(dao);
        Mockito.verify(sensorChanged).fire(sensor);
    }

    @Test
    public void updateSensor_UnitDoesntBelongToUser() {
        assertThrows(TechnicalException.class, () -> {
            Sensor sensor = newSensorWithNotBelongingUnit();

            service.updateSensor(sensor);
            Mockito.verifyNoMoreInteractions(sensorChanged);
        });
    }

    @Test
    public void getAllSensorForUser() {
        service.getAllSensorsForUser();

        Mockito.verify(dao).getAllSensorsForUser(user);
    }

    private User newUser() {
        User user = new User();
        user.setId(-1L);
        this.unit = new Unit();
        unit.setId(1337L);
        unit.setUser(user);
        user.setUnitList(Collections.singletonList(unit));
        return user;
    }

    private Sensor newSensor() {
        Sensor sensor = new Sensor();
        sensor.setId(SENSOR_ID);
        sensor.setUnit(unit);
        return sensor;
    }

    private Sensor newSensorWithNotBelongingUnit() {
        Sensor sensor = new Sensor();
        Unit unit = new Unit();
        unit.setId(-1L);
        sensor.setUnit(unit);
        return sensor;
    }
}
