package com.gepardec.hogarama.rest.interceptor;

import com.gepardec.hogarama.domain.owner.OwnerService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

@Provider
@DetermineOwner
public class DetermineOwnerInterceptor implements ContainerRequestFilter {

    @Inject
    private OwnerService service;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) {
        SecurityContext sc = containerRequestContext.getSecurityContext();
        if (sc != null && sc.getUserPrincipal() instanceof KeycloakPrincipal) {
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc.getUserPrincipal();
            String ssoUserId = kp.getName();
            if (!service.isRegistered(ssoUserId)) {
                service.register(ssoUserId);
            }
        }
    }
}
