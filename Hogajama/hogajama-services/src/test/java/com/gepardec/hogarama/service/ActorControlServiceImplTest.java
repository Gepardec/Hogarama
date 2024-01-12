package com.gepardec.hogarama.service;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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
      Assertions.assertEquals("Supplied parameters 'null', 'null', '5' must not be empty or null", e.getMessage());
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
      Assertions.assertEquals("Supplied parameters '', '', '5' must not be empty or null", e.getMessage());
    }
  }
}
