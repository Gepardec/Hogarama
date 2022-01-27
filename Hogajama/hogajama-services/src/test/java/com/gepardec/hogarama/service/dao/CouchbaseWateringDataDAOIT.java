package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.annotations.CouchbaseDAO;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.watering.WateringData;
import com.gepardec.hogarama.service.CouchbaseProducer;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.gepardec.hogarama.domain.DateUtils.getMax;
import static com.gepardec.hogarama.domain.DateUtils.getMin;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddBeanClasses({CouchbaseWateringDataDAO.class, CouchbaseProducer.class})
public class CouchbaseWateringDataDAOIT {

  private static final String DEMO_PFLANZE       = "Demo-Pflanze";
  private static final String PFLANZE_NOT_EXISTS = "Irgendein-Gestrüpp";

  @Inject
  @CouchbaseDAO
  private CouchbaseWateringDataDAO classUnderTest;

  @Test
  public void testGetWateringData_OK() {
    List<WateringData> result = classUnderTest.getWateringData(1, DEMO_PFLANZE, getMin(), getMax());
    assertNotNull(result);
    assertEquals(1, result.size());

    WateringData data = result.get(0);
    assertEquals(DEMO_PFLANZE, data.getName());

    Date time = data.getTime();
    assertFalse(time.before(getMin()));
    assertFalse(time.after(getMax()));
  }

  @Test
  public void testGetWateringData_TechnicalException() {
    assertThrows(TechnicalException.class, () -> classUnderTest.getWateringData(null, DEMO_PFLANZE, getMin(), getMax()));
  }

  @Test
  public void testGetWateringData_NameNotExists() {
    List<WateringData> result = classUnderTest.getWateringData(1, PFLANZE_NOT_EXISTS, getMin(), getMax());
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testGetWateringData_OutsideTimerange() {
    List<WateringData> result = classUnderTest.getWateringData(1, PFLANZE_NOT_EXISTS, getMin(), getMin());
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  public void testSaveWateringData_OK() {
    Date date = new Date(System.currentTimeMillis());
    String id = UUID.randomUUID().toString();

    classUnderTest.save(new WateringData(id, date, DEMO_PFLANZE, "Wien", 6));

    List<WateringData> allData = classUnderTest.getWateringData(1, DEMO_PFLANZE, date, date, true);
    assertNotNull(allData);
    assertEquals(1, allData.size());
    assertEquals(id, allData.get(0).getId());
    assertEquals(DEMO_PFLANZE, allData.get(0).getName());
  }

  @Test
  public void testSaveWateringData_ThrowTechnicalException() {
    Date date = new Date(System.currentTimeMillis());
    String id = UUID.randomUUID().toString();
    WateringData data = new WateringData(id, date, DEMO_PFLANZE, "Wien", 6);

    classUnderTest.save(data);
    assertThrows(TechnicalException.class, () ->  classUnderTest.save(data));
  }

}
