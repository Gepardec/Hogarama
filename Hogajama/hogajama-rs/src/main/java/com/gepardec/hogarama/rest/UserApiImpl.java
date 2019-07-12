package com.gepardec.hogarama.rest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class UserApiImpl implements UserApi {

    @Context
    SecurityContext sc;

    @Override
    public Response getUser(SecurityContext securityContext) {
        final UserData userData = new UserData();
        if (sc.getUserPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc.getUserPrincipal();
            final AccessToken token = kp.getKeycloakSecurityContext().getToken();
            userData.setName(token.getName());
            userData.setEmail(token.getEmail());
            userData.setFamilyName(token.getFamilyName());
            userData.setGivenName(token.getGivenName());
        }
        return Response.ok(userData).build();
    }
}
