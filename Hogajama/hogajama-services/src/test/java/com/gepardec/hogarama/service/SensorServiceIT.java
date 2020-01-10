package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.Sensor;
import com.gepardec.hogarama.domain.entity.SensorType;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.support.BeforeWarp;
import org.dcm4che.test.support.WarpUnitTest;
import org.dcm4che.test.support.WarpUnitTestConfig;
import org.junit.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;


@WarpUnitTestConfig(baseRestUrl = "http://localhost:8080/hogajama-rs/rest/")
public class SensorServiceIT extends WarpUnitTest implements Serializable {

    public static final String TEST_OWNER = "TEST_OWNER";
    @Inject
    private OwnerService ownerService;

    @Inject
    private SensorService service;

    @Inject
    private OwnerStore store;

    @Test
    public void getAllSensors() throws IOException {
        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner(TEST_OWNER);
            SensorType sensorType = manipulator.loadEntity(1L, SensorType.class);

            Sensor sensor = new Sensor();
            sensor.setName("MySensor1");
            sensor.setDeviceId("MySensorDeviceId");
            sensor.setSensorType(sensorType);
            sensor.setUnit(owner.getDefaultUnit());
            manipulator.insertEntity(sensor);

            List<Sensor> sensorList = warp(() -> service.getAllSensors());
            assertThat(sensorList).hasSize(1);
            assertThat(sensorList.get(0))
                    .extracting(Sensor::getName).isEqualTo("MySensor1");
        }
    }

    @Test
    public void getAllSensorForOwner() throws IOException {
        warpMeta.setExecuteBeforeWarp(true);
        warpMeta.setExecuteInTransaction(true);

        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner(TEST_OWNER);
            SensorType sensorType = manipulator.loadEntity(1L, SensorType.class);

            Sensor sensor1 = new Sensor();
            sensor1.setName("MySensor1");
            sensor1.setDeviceId("MySensorDeviceId1");
            sensor1.setSensorType(sensorType);
            sensor1.setUnit(owner.getDefaultUnit());
            manipulator.insertEntity(sensor1);

            Sensor sensor2 = new Sensor();
            sensor2.setName("MySensor2");
            sensor2.setDeviceId("MySensorDeviceId2");
            sensor2.setSensorType(sensorType);
            sensor2.setUnit(owner.getDefaultUnit());
            manipulator.insertEntity(sensor2);

            List<Sensor> sensorList = warp(() -> service.getAllSensors());
            assertThat(sensorList).hasSize(2);
            assertThat(sensorList.get(0))
                    .extracting(Sensor::getName).isEqualTo("MySensor1");
            assertThat(sensorList.get(1))
                    .extracting(Sensor::getName).isEqualTo("MySensor2");
        }
    }

    @Test
    public void createSensor() throws IOException {
        warpMeta.setExecuteBeforeWarp(true);
        warpMeta.setExecuteInTransaction(true);

        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner(TEST_OWNER);
            SensorType sensorType = manipulator.loadEntity(1L, SensorType.class);

            Sensor sensor = new Sensor();
            sensor.setName("MySensor1");
            sensor.setDeviceId("MySensorDeviceId");
            sensor.setSensorType(sensorType);
            sensor.setUnit(owner.getDefaultUnit());
            warp(() -> service.createSensor(sensor));
            Sensor result = manipulator.loadEntityByQuery("select s from Sensor as s where s.name = 'MySensor1'", Sensor.class);

            assertThat(result.getDeviceId()).isEqualTo("MySensorDeviceId");
            getEntityManipulator().removeEntityWithoutRestoringById(result.getId(), Sensor.class);
        }
    }

    @Test
    public void createSensorForDefaultUnit() throws IOException {
        warpMeta.setExecuteBeforeWarp(true);
        warpMeta.setExecuteInTransaction(true);

        try (EntityManipulator manipulator = getEntityManipulator()) {
            Owner owner = createTestOwner(TEST_OWNER);
            SensorType sensorType = manipulator.loadEntity(1L, SensorType.class);

            Sensor sensor = new Sensor();
            sensor.setName("MySensor1");
            sensor.setDeviceId("MySensorDeviceId");
            sensor.setSensorType(sensorType);
            warp(() -> service.createSensorForDefaultUnit(sensor));
            Sensor result = manipulator.loadEntityByQuery("select s from Sensor as s where s.name = 'MySensor1'", Sensor.class);

            assertThat(result.getDeviceId()).isEqualTo("MySensorDeviceId");
            assertTrue(result.getUnit().isDefaultUnit());
            getEntityManipulator().removeEntityWithoutRestoringById(result.getId(), Sensor.class);
        }
    }

    private Owner createTestOwner(String ssoUserId) {
        Owner registeredOwner = warp(() -> {
            Owner owner = ownerService.register(ssoUserId);
            return owner;
        });
        cleanUp(() -> {
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner.getDefaultUnit());
            registeredOwner.setUnitList(null);
            getEntityManipulator().removeEntityWithoutRestoring(registeredOwner);
        });
        return registeredOwner;
    }

    @BeforeWarp
    private void fillOwnerStore() {
        store.setOwner(ownerService.getRegisteredOwner(TEST_OWNER).orElse(null));
    }
}
