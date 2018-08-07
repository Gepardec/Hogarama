package com.gepardec.hogarama.service;

import org.junit.Ignore;
import org.junit.Test;

public class PumpServiceImplTest {

  @Test
  @Ignore
  public void testSendMessage() {

    ActorServiceImpl service = new ActorServiceImpl();
    service.sendActorMessage("Wien", "GruenerGepard", 25);

  }
}
