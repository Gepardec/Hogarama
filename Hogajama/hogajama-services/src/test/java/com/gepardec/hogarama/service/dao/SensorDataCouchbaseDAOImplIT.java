package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.sensor.SensorData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@EnableAutoWeld
public class SensorDataCouchbaseDAOImplIT {

  @Inject
  private SensorDataCouchbaseDAOImpl classUnderTest;

  private static final String SENSOR_NAME_DEFAULT = "Demo-Pflanze";
  private static final String SENSOR_NAME_MULTI = "Demo-Pflanze-multi-location";
  private static final String SENSOR_NAME_NONEXSISTING = "NonExistingSensorName";

  @Test
  public void testGetAllSensors_OK() {
    List<String> allSensors = classUnderTest.getAllSensors();
    assertNotNull(allSensors);
    assertTrue(allSensors.containsAll(Arrays.asList(SENSOR_NAME_MULTI, SENSOR_NAME_DEFAULT)));
  }

  @Test
  public void testGetAllData_OK() {
    Date from = getDate(2021, 1, 1);
    Date to = getDate(2022, 12, 31);

    List<SensorData> allData = classUnderTest.getAllData(2, SENSOR_NAME_DEFAULT, from, to);
    assertNotNull(allData);
    assertEquals(2, allData.size());
    for (SensorData data : allData) {
      assertEquals(SENSOR_NAME_DEFAULT, data.getSensorName());
      assertFalse(data.getTime().before(from)); // same or after FROM
      assertFalse(data.getTime().after(to)); // same or before TO
    }
  }

  @Test
  public void testSaveSensorData_OK() {
    Date date = new Date(System.currentTimeMillis());
    String id = UUID.randomUUID().toString();

    classUnderTest.save(new SensorData(id, date, SENSOR_NAME_DEFAULT, "wasser", 0.2, "Wien", "0"));

    List<SensorData> allData = classUnderTest.getAllData(1, SENSOR_NAME_DEFAULT, date, date, true);
    assertNotNull(allData);
    assertEquals(1, allData.size());
    assertEquals(id, allData.get(0).getId());
    assertEquals(SENSOR_NAME_DEFAULT, allData.get(0).getSensorName());
  }

  @Test
  public void testGetLocationBySensorName_OK() {
    String locationBySensorName = classUnderTest.getLocationBySensorName(SENSOR_NAME_DEFAULT);
    assertEquals("Wien", locationBySensorName);
  }

  @Test
  public void testGetLocationBySensorName_NotExists() {
    assertThrows(NoResultException.class, () ->
        classUnderTest.getLocationBySensorName(SENSOR_NAME_NONEXSISTING));
  }

  @Test
  public void testGetLocationBySensorName_NonUniqueResult() {
    assertThrows(NonUniqueResultException.class, () ->
        classUnderTest.getLocationBySensorName(SENSOR_NAME_MULTI));
  }

  private Date getDate(int year, int month, int dayOfMonth) {
    return Date.from(LocalDate.of(year, month, dayOfMonth).atStartOfDay(ZoneId.systemDefault()).toInstant());
  }
}
