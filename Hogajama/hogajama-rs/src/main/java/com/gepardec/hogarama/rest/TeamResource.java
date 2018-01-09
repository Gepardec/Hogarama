package com.gepardec.hogarama.rest;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;

@Path("team")
public class TeamResource {
	private static final String TEAM_URL = "http://www.gepardec.com/team/";

	@GET
	@Produces("text/html")
	public String getTeamMembers() throws IOException {
		URL url = new URL(TEAM_URL);
		URLConnection connection = url.openConnection();
		return IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8.name());
	}
}
