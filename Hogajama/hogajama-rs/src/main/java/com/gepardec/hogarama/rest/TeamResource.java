package com.gepardec.hogarama.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.apache.commons.io.IOUtils;

@Path("team")
public class TeamResource {
	private static final String TEAM_URL = "http://www.gepardec.com/team/";

	@GET
	@Produces("text/html")
	public String getTeamMembers() throws IOException {
		URL url = new URL(TEAM_URL);
		URLConnection connection = url.openConnection();
		try(InputStream is =  connection.getInputStream()){
			return IOUtils.toString(is, StandardCharsets.UTF_8.name());
		}
	}
}
