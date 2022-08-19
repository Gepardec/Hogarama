package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.rest.unitmanagement.BaseResponse;
import com.gepardec.hogarama.rest.unitmanagement.dto.ClientConfig;
import org.apache.http.HttpStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("clientconfig")
public class ClientConfigResource {

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAuthServerUrl() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setAuthServerUrl(System.getenv("KEYCLOAK_AUTH_SERVER_URL"));
		clientConfig.setRealm(System.getenv("KEYCLOAK_REALM"));
		clientConfig.setClientIdFrontend(System.getenv("KEYCLOAK_CLIENT_ID_FRONTEND"));
		return new BaseResponse<>(clientConfig, HttpStatus.SC_OK).createRestResponse();
	}
}
