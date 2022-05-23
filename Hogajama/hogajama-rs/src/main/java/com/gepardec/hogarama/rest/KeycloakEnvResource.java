package com.gepardec.hogarama.rest;

import com.gepardec.hogarama.rest.unitmanagement.BaseResponse;
import com.gepardec.hogarama.rest.unitmanagement.dto.KeycloakDto;
import org.apache.http.HttpStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("keycloak")
public class KeycloakEnvResource {

	@GET
	@Path("")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAuthServerUrl() {
		KeycloakDto keycloakDto = new KeycloakDto();
		keycloakDto.setAuthServerUrl(System.getenv("KEYCLOAK_AUTH_SERVER_URL"));
		keycloakDto.setRealm(System.getenv("KEYCLOAK_REALM"));
		keycloakDto.setClientId(System.getenv("KEYCLOAK_CLIENT_ID"));
		keycloakDto.setCredentialsSecret(System.getenv("KEYCLOAK_CREDENTIALS_SECRET"));
		return new BaseResponse<>(keycloakDto, HttpStatus.SC_OK).createRestResponse();
	}
}
