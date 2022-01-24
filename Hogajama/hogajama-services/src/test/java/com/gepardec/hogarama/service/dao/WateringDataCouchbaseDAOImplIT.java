package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.watering.WateringData;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static com.gepardec.hogarama.domain.DateUtils.getMax;
import static com.gepardec.hogarama.domain.DateUtils.getMin;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
public class WateringDataCouchbaseDAOImplIT {

  private static final String DEMO_PFLANZE       = "Demo-Pflanze";
  private static final String PFLANZE_NOT_EXISTS = "Irgendein-Gestrüpp";

  @Inject
  private WateringDataCouchbaseDAOImpl classToTest;

  @Test
  public void testGetWateringData_OK() {
    List<WateringData> result = classToTest.getWateringData(1, DEMO_PFLANZE, getMin(), getMax());
    assertNotNull(result);
    assertEquals(1, result.size());

    WateringData data = result.get(0);
    assertEquals(DEMO_PFLANZE, data.getName());

    Date time = data.getTime();
    assertFalse(time.before(getMin()));
    assertFalse(time.after(getMax()));
  }

  @Test
  public void testGetWateringData_NameNotExists() {
    List<WateringData> result = classToTest.getWateringData(1, PFLANZE_NOT_EXISTS, getMin(), getMax());
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetWateringData_OutsideTimerange() {
    List<WateringData> result = classToTest.getWateringData(1, PFLANZE_NOT_EXISTS, getMin(), getMin());
    assertNotNull(result);
    assertEquals(0, result.size());
  }

}
