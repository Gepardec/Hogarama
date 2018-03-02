package com.gepardec.hogarama.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("openshift")
public class OpenshiftEnvResource {

	@GET
	@Path("stage")
	@Produces("text/html")
	public String getStage() {
		return getReturnValue(System.getenv("STAGE"));
	}

	@GET
	@Path("tinyurl")
	@Produces("text/html")
	public String getTinyUrl() {
		return getReturnValue(System.getenv("TINYURL"));
	}

	@GET
	@Path("hostname")
	@Produces("text/html")
	public String getHostname() {
		String ret = System.getenv("HOSTNAME");
		if (ret == null) {
			return "Not found";
		}

		if (ret.split("-").length != 3) {
			return ret;
		} else {
			return "" + ret.split("-")[2].hashCode();
		}

	}
	
	private String getReturnValue(String ret) {
		return ret != null ? ret : "Not found";
	}
}
