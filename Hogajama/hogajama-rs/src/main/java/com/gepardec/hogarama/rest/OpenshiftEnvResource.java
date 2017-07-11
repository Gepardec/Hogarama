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

    @GET
    @Path("hostname")
    @Produces("text/html")
    public String getHostname() throws IOException {

        String ret = System.getenv("HOSTNAME");
        if(ret == null){
            return "Not found";
        }

        if(ret.split("-").length != 3){
            return ret;
        } else {
            return "" + ret.split("-")[2].hashCode();
        }

        //return "Not found";
    }

}
