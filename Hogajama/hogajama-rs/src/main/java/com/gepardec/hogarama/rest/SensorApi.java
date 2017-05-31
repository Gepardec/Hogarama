package com.gepardec.hogarama.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.gepardec.hogarama.domain.SensorData;

@Path("/sensor")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@io.swagger.annotations.Api(description = "the sensor API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyEapServerCodegen", date = "2017-05-26T10:41:51.293+02:00")
public interface SensorApi  {
   
    @GET
    @Path("/allData")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "Returns all sensors", response = SensorData.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "sensor data response", response = SensorData.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "unexpected error", response = SensorData.class, responseContainer = "List") })
    public Response getAllDataMaxNumber(  @QueryParam("maxNumber") Integer maxNumber,  @QueryParam("sensor") String sensor,@Context SecurityContext securityContext);
    
    @GET
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @io.swagger.annotations.ApiOperation(value = "", notes = "Returns all sensor-names", response = String.class, responseContainer = "List", tags={  })
    @io.swagger.annotations.ApiResponses(value = { 
        @io.swagger.annotations.ApiResponse(code = 200, message = "sensor names response", response = String.class, responseContainer = "List"),
        
        @io.swagger.annotations.ApiResponse(code = 200, message = "unexpected error", response = String.class, responseContainer = "List") })
    public Response getAllSensors(@Context SecurityContext securityContext);
}