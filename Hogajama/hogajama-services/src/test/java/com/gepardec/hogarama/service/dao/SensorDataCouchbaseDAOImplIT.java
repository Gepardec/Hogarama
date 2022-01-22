package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.sensor.SensorData;
import com.gepardec.hogarama.service.couchbase.CouchbaseProducer;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.jboss.weld.junit5.auto.AddBeanClasses;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


@EnableAutoWeld
@AddBeanClasses(CouchbaseProducer.class)
public class SensorDataCouchbaseDAOImplIT {

  @Test
  @Disabled
  public void testGetAllData_OK() {
    Calendar cal = Calendar.getInstance();
    cal.set(2021, Calendar.JANUARY, 1);
    Date from = cal.getTime();
    cal.set(2021, Calendar.DECEMBER, 12);
    Date to = cal.getTime();

    List<SensorData> allData = new SensorDataCouchbaseDAOImpl().getAllData(0, "Pflanze-mit-time", from, to);
    Assertions.assertNotNull(allData);
  }
}
