package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.Sensor;
import com.gepardec.hogarama.domain.entity.SensorType;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.support.WarpUnitTest;
import org.dcm4che.test.support.WarpUnitTestConfig;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@WarpUnitTestConfig(baseRestUrl = "http://localhost:8080/hogajama-rs/rest/")
public class SensorServiceIT extends WarpUnitTest implements Serializable {

    @Inject
    private OwnerService ownerService;

    @Inject
    private SensorService service;

    @Inject
    private OwnerStore store;

    @Test
    public void getAllSensors() throws IOException {
        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner("TEST_OWNER");
            SensorType sensorType = manipulator.loadEntity(1L, SensorType.class);

            Sensor sensor1 = new Sensor();
            sensor1.setName("MySensor1");
            sensor1.setDeviceId("MySensorDeviceId");
            sensor1.setSensorType(sensorType);
            sensor1.setUnit(owner.getDefaultUnit());
            Sensor sensor = sensor1;
            manipulator.insertEntity(sensor);

            List<Sensor> sensorList = warp(() -> service.getAllSensors());
            assertThat(sensorList).hasSize(1);
            assertThat(sensorList.get(0))
                    .extracting(Sensor::getName).isEqualTo("MySensor1");
        }
    }

    @Test
    public void getAllSensorForOwner() {
    }

    @Test
    public void createSensor() throws IOException {
        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner("TEST_OWNER");
            SensorType sensorType = manipulator.loadEntity(1L, SensorType.class);

            Sensor sensor1 = new Sensor();
            sensor1.setName("MySensor1");
            sensor1.setDeviceId("MySensorDeviceId");
            sensor1.setSensorType(sensorType);
            sensor1.setUnit(owner.getDefaultUnit());
            Sensor sensor = sensor1;
            warp(() -> service.createSensor(sensor));
            Sensor result = manipulator.loadEntityByQuery("select s from Sensor as s where s.name = 'MySensor1'", Sensor.class);
            System.out.println("ASd");
        }
    }

    @Test
    public void createSensorForDefaultUnit() {
    }

    private Owner createTestOwner(String ssoUserId) {
        Owner registeredOwner = warp(() -> {
            Owner owner = ownerService.register(ssoUserId);
            store.setOwner(owner);
            return owner;
        });
        cleanUp(() -> {
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner.getDefaultUnit());
            registeredOwner.setUnitList(null);
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner);
        });
        return registeredOwner;
    }
}
