package com.gepardec.hogarama.service;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActorControlServiceImplTest {

  @InjectMocks
  private ActorControlServiceImpl actorService;

  @Test
  public void testCheckParametersOrFailOk() {
    String sensorName = "Palmlilie";
    String location = "Vienna";

    actorService.checkParametersOrFail(location, sensorName, 5);
  }

  @Test
  public void testCheckParametersOrFailNoParameters() {
    String sensorName = null;
    String location = null;

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

    try {
      actorService.checkParametersOrFail(location, sensorName, 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      Assert.assertEquals("Supplied parameters '', '', '5' must not be empty or null", e.getMessage());
    }
  }
}
