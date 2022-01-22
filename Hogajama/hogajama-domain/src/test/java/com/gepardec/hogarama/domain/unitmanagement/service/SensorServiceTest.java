package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.SensorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.Optional;
import javax.enterprise.event.Event;

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
    private Owner owner;

    @BeforeEach
    public void setUp() {
        owner = newOwner();
        Mockito.lenient().when(userContext.getOwner()).thenReturn(owner);
    }

    @Test
    public void createSensor_OK() {
        Sensor sensor = newSensor();

        service.createSensor(sensor);

        Mockito.verify(dao).save(sensor);
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void createSensor_UnitDoesntBelongToOwner() {
        Sensor sensor = newSensorWithNotBelongingUnit();

        Assertions.assertThrows(TechnicalException.class, () -> service.createSensor(sensor));
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
    public void updateSensor_UnitDoesntBelongToOwner() {
        Sensor sensor = newSensorWithNotBelongingUnit();

        Assertions.assertThrows(TechnicalException.class, () -> service.updateSensor(sensor));
        Mockito.verifyNoMoreInteractions(sensorChanged);
    }

    @Test
    public void getAllSensorForOwner() {
        service.getAllSensorsForOwner();

        Mockito.verify(dao).getAllSensorsForOwner(owner);
    }

    private Owner newOwner() {
        Owner owner = new Owner();
        this.unit = new Unit();
        unit.setId(1337L);
        owner.setUnitList(Collections.singletonList(unit));
        return owner;
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
