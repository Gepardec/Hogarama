package com.gepardec.hogarama.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("openshift")
public class OpenshiftEnvResource {

	@GET
	@Path("stage")
	@Produces("text/html")
	public String getStage() throws IOException {

	    String ret = System.getenv("STAGE");

		return ret != null ? ret : "Not found";
	}

}
