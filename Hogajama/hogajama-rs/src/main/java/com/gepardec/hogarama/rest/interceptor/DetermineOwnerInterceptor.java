package com.gepardec.hogarama.rest.interceptor;


import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.service.OwnerService;
import com.gepardec.hogarama.service.OwnerStore;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.Optional;

@DetermineOwner
@Interceptor
public class DetermineOwnerInterceptor {

    @Inject
    private OwnerService service;
    @Inject
    private OwnerStore store;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        SecurityContext sc = extractSecurityContext(ctx);

        if (sc != null && sc.getUserPrincipal() instanceof KeycloakPrincipal) {
            @SuppressWarnings("unchecked")
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc.getUserPrincipal();
            String ssoUserId = kp.getName();
            Optional<Owner> optionalOwner = service.getRegisteredOwner(ssoUserId);
            Owner owner = optionalOwner.orElseGet(() -> registerOwnerAndHandleDuplicates(ssoUserId));
            store.setOwner(owner);
        }

        return ctx.proceed();
    }

    private Owner registerOwnerAndHandleDuplicates(String ssoUserId) {
        try {
            return service.register(ssoUserId);
        } catch (Exception e) {
            if (e.getCause().getMessage().contains("ConstraintViolationException")) {
                return service.getRegisteredOwner(ssoUserId).orElse(null);
            }
            throw e;
        }
    }

    private SecurityContext extractSecurityContext(InvocationContext ctx) {
        return Arrays.stream(ctx.getParameters())
                .filter(p -> SecurityContext.class.isAssignableFrom(p.getClass()))
                .map(p -> (SecurityContext) p)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Security context supplied"));
    }
}
