package com.gepardec.hogarama.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import com.gepardec.hogarama.domain.Habarama;
//import com.gepardec.hogarama.service.dao.HabaramaDAO;
import com.gepardec.hogarama.service.dao.HabaramaDAO;

/**
 * Root resource (exposed at "helloworld" path)
 */
@Path("helloworld")
public class HelloWorld {
	@Context
	private UriInfo context;
	
	@Inject
	private HabaramaDAO habaramaDAO;
	
	private final String USER_AGENT = "Mozilla/5.0";
	
	/** Creates a new instance of HelloWorld */
	public HelloWorld() {
	}

	/**
	 * Retrieves representation of an instance of helloWorld.HelloWorld
	 * 
	 * @return an instance of java.lang.String
	 */
	@GET
	@Produces("text/html")
	public String getHtml() {
		return "<html lang=\"en\"><body><h1>Hogarama!</h1></body></html>";
	}
	
	@GET
	@Path("mongodb/{maxNumber}")
	@Produces("text/html")
	public String getMongoDb( @PathParam("maxNumber") int maxNumber ) {
		String habarama = habaramaDAO.query(maxNumber);
		return habarama;
	}
	
	@GET
	@Path("mongodb/")
	@Produces("text/html")
	public String getMongoDb( ) {
		String habarama = habaramaDAO.query();
		return habarama;
	}
	
	
	
	@GET
	@Path("team-members")
	@Produces("text/html")
	public String getTeamMembers() throws IOException {
		String url = "http://www.gepardec.com/team/";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
//		System.out.println(response.toString());
		return response.toString();
	}
	
}