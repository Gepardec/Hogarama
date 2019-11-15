package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.sensor.SensorDataDAO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ActorServiceImplTest {

  @Mock
  private SensorDataDAO sensorDataDAO;

  @InjectMocks
  private ActorServiceImpl actorService;

  @Test
  public void testCheckParametersOrFailOk() {
    String sensorName = "Palmlilie";
    String location = "Vienna";

    Mockito.when(sensorDataDAO.getLocationBySensorName(sensorName)).thenReturn(location);

    actorService.checkParametersOrFail(location, sensorName, 5);
  }

  @Test
  public void testCheckParametersOrFailNoParameters() {
    String sensorName = null;
    String location = null;

    Mockito.when(sensorDataDAO.getLocationBySensorName(sensorName)).thenReturn(location);

    try {
      actorService.checkParametersOrFail(location, sensorName, 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      Assert.assertEquals("Supplied parameters 'null', 'null', '5' must not be empty or null", e.getMessage());
    }
  }

  @Test
  public void testCheckParametersOrFailEmptyParameters() {
    String sensorName = "";
    String location = "";

    Mockito.when(sensorDataDAO.getLocationBySensorName(sensorName)).thenReturn(location);

    try {
      actorService.checkParametersOrFail(location, sensorName, 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      Assert.assertEquals("Supplied parameters '', '', '5' must not be empty or null", e.getMessage());
    }
  }

  @Test
  public void testCheckParametersOrFailWrongLocation() {
    String sensorName = "Palmlilie";
    String location = "Vienna";

    Mockito.when(sensorDataDAO.getLocationBySensorName(sensorName)).thenReturn("Linz");

    try {
      actorService.checkParametersOrFail(location, sensorName, 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      Assert.assertEquals("For sensor Palmlilie location must be 'Linz' but was 'Vienna'", e.getMessage());
    }
  }

  @Test
  public void testCheckParametersOrFailNoRegisteredLocation() {
    String sensorName = "Palmlilie";
    String location = "Vienna";

    Mockito.when(sensorDataDAO.getLocationBySensorName(sensorName)).thenReturn(null);

    try {
      actorService.checkParametersOrFail(location, sensorName, 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      Assert.assertEquals("Palmlilie is not a registered sensor.", e.getMessage());
    }
  }
}
