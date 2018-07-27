package com.gepardec.hogarama.service;

import org.junit.Ignore;
import org.junit.Test;

public class PumpServiceImplTest {

  @Test
  @Ignore
  public void testSendMessage() {

    PumpServiceImpl service = new PumpServiceImpl();
    service.sendPumpMessage("Wien", "GruenerGepard", 25);

  }
}
