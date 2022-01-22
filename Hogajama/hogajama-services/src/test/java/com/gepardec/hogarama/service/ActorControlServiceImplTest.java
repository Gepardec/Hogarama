package com.gepardec.hogarama.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
    actorService.checkParametersOrFail("Vienna", "Palmlilie", 5);
  }

  @Test
  public void testCheckParametersOrFailNoParameters() {
    try {
      actorService.checkParametersOrFail(null, null, 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      assertEquals("Supplied parameters 'null', 'null', '5' must not be empty or null", e.getMessage());
    }
  }

  @Test
  public void testCheckParametersOrFailEmptyParameters() {
    try {
      actorService.checkParametersOrFail("", "", 5);
      fail("Expected exception due to missing parameters.");
    } catch(IllegalArgumentException e) {
      assertEquals("Supplied parameters '', '', '5' must not be empty or null", e.getMessage());
    }
  }
}
