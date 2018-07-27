package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.service.PumpService;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class PumpApiImpl implements PumpApi {

  @Inject PumpService pumpService;

  @Override
  public Response sendPumpMessage(String location, String sensorName, Integer duration, SecurityContext securityContext) {
    pumpService.sendPumpMessage(location, sensorName, duration);

    return Response.ok().build();
  }
}
