package com.gepardec.hogarama.domain.watering;

import com.gepardec.hogarama.dao.dummydata.DummySensorDAO;
import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.domain.sensor.SensorNormalizer;
import com.gepardec.hogarama.domain.unitmanagement.cache.ActorCache;
import com.gepardec.hogarama.domain.unitmanagement.cache.SensorCache;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Sensor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.gepardec.hogarama.testdata.TestDataProducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static com.gepardec.hogarama.domain.DateUtils.toDate;
import static org.junit.jupiter.api.Assertions.*;

public class WateringServiceTest {

    private static final String SENSOR_DEVICE_ID = "SensorDeviceId";
    private static final String SENSOR_NAME = "SensorName";
    private static final String UNIT_NAME = "SensorUnit";
    private static final String ACTOR_DEVICE_ID = "ActorDeviceId";
    private static final String ACTOR_NAME = "ActorName";

    private ActorCache actorCache;
    private SensorCache sensorCache;
    private SensorNormalizer sensorNormalizer;
    private WateringService watering;
    private MockActorControlService actorSvc;
    private InMemoryWateringConfigDAO wateringConfigDao;
    private TestDataProducer data;

    @BeforeEach
    public void setUp() {
        actorCache = Mockito.mock(ActorCache.class);
        sensorCache = Mockito.mock(SensorCache.class);
        sensorNormalizer = Mockito.mock(SensorNormalizer.class);
        Mockito.when(actorCache.getByDeviceId(Mockito.any())).thenReturn(Optional.of(newActor()));
        Mockito.when(sensorCache.getByDeviceId(Mockito.any())).thenReturn(Optional.of(newSensor()));
        Mockito.when(sensorNormalizer.normalize(Mockito.any(SensorData.class))).thenAnswer(((Answer<SensorData>) invocationOnMock -> (SensorData) invocationOnMock.getArguments()[0]));
    }

    private Sensor newSensor() {
        Sensor sensor = new Sensor();
        sensor.setDeviceId(SENSOR_DEVICE_ID);
        sensor.setName(SENSOR_NAME);
        Unit unit = new Unit();
        unit.setName(UNIT_NAME);
        sensor.setUnit(unit);
        return sensor;
    }

    private Actor newActor() {
        Actor actor = new Actor();
        actor.setDeviceId(ACTOR_DEVICE_ID);
        actor.setName(ACTOR_NAME);
        return actor;
    }

    private void setupWatering(MockActorControlService actorSvc, InMemoryWateringConfigDAO wateringConfigDao) {
        data = new TestDataProducer(startSensorData());
        data.addValueMinusMinutes(0.1, 10);
        data.addValueMinusMinutes(0.1, 10);
        data.addValueAt(0.6, LocalDateTime.of(2019, Month.JUNE, 20, 14, 00));

        DummySensorDAO sensorDao = new DummySensorDAO(data.getData());

        watering = new WateringService(sensorDao, sensorNormalizer, actorSvc, new WateringStrategy(), wateringConfigDao, actorCache, sensorCache);

    }

    private void setupWatering() {
        actorSvc = new MockActorControlService("Vienna", "My Plant", WateringService.Config.DEFAULT.waterDuration);
        wateringConfigDao = new InMemoryWateringConfigDAO();
        setupWatering(actorSvc, wateringConfigDao);
    }

    @Test
    public void testWateringOfMyPlant() {
        setupWatering();
        waterAll();
        assertTrue(actorSvc.wasCalled(), "Actor was called");
    }

    @Test
    public void testChangeDefaultConfiguration() {

        InMemoryWateringConfigDAO wateringConfigDao = new InMemoryWateringConfigDAO();
        WateringConfigData wconfig = new WateringConfigData("My Plant", "My Plant", 0.2, 6);
        wateringConfigDao.save(wconfig);
        MockActorControlService actor = new MockActorControlService("Vienna", "My Plant", 6);

        setupWatering(actor, wateringConfigDao);
        waterAll();
        assertTrue(actor.wasCalled(), "Actor was called");
    }

    @Test
    public void testUseDifferentActorNameThanSensorName() {

        InMemoryWateringConfigDAO wateringConfigDao = new InMemoryWateringConfigDAO();
        WateringConfigData wconfig = new WateringConfigData("My Plant", "My Actor", 0.2, 6);
        wateringConfigDao.save(wconfig);
        MockActorControlService actor = new MockActorControlService("Vienna", "My Actor", 6);

        setupWatering(actor, wateringConfigDao);
        waterAll();
        assertTrue(actor.wasCalled(), "Actor was called");
    }

    @Test
    public void testWateringWillSaveDefaultConfig() {
        setupWatering();
        waterAll();

        assertNotNull(wateringConfigDao.getBySensorName("My Plant"));
    }

    @Test
    public void testLowValueWillTriggerWatering() {
        setupWatering();
        SensorData val = data.getNext();
        assertEquals(0.1, val.getValue(), 0.01);
        watering.processSensorData(val);
        assertTrue(actorSvc.wasCalled(), "Actor was called");
    }

    @Test
    public void testHighValueWontTriggerWatering() {
        setupWatering();
        data.getNext();
        data.getNext();
        data.getNext();
        SensorData val = data.getNext();
        assertEquals(0.6, val.getValue(), 0.01);
        watering.processSensorData(val);
        assertFalse(actorSvc.wasCalled(), "Actor was not called");
    }

    @Test
    public void testStrategieUsesAverage() {
        setupWatering();
        watering.processSensorData(data.getNext().setValue(0.4));
        assertFalse(actorSvc.wasCalled(), "Actor was not called with 0.4");
        watering.processSensorData(data.getNext().setValue(0.1));
        assertFalse(actorSvc.wasCalled(), "Actor was not called with 0.1");
        watering.processSensorData(data.getNext().setValue(0.1));
        assertFalse(actorSvc.wasCalled(), "Actor was not called with second 0.1");
        watering.processSensorData(data.getNext().setValue(0.1));
        assertTrue(actorSvc.wasCalled(), "Actor was called with third 0.1");
    }

    private SensorData startSensorData() {
        return new SensorData(
                "1",
                toDate(LocalDateTime.of(2018, Month.JUNE, 20, 15, 00)),
                "My Plant",
                "noramlised",
                0.1,
                "Vienna",
                "1.0");
    }

    private void waterAll() {
        data.waterAll(watering);
    }

    private static class MockActorControlService implements ActorControlService {

        private final String location;
        private final String actorName;
        private final Integer duration;
        private boolean wasCalled = false;

        public MockActorControlService(String location, String actorName, Integer duration) {
            this.location = location;
            this.actorName = actorName;
            this.duration = duration;
        }

        public boolean wasCalled() {
            return wasCalled;
        }

        @Override
        public void sendActorMessage(WateringData actorData) {
            assertEquals(this.location, actorData.getLocation());
            assertEquals(this.actorName, actorData.getName());
            assertEquals(this.duration, actorData.getDuration());
            wasCalled = true;
        }

    }
}

