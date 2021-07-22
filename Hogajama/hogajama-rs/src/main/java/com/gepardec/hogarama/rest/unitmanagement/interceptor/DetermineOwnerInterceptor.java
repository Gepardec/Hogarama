package com.gepardec.hogarama.rest.unitmanagement.interceptor;


import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.domain.unitmanagement.service.OwnerService;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.Optional;

/**
 * Intercepts all requests annotated with {@link DetermineOwner} and extracts logged in user
 * by request's bearer token. <br />
 * The extracted user will be stored in the {@link UserContext}.
 */
@DetermineOwner
@Interceptor
public class DetermineOwnerInterceptor {

    private static final String HOGAJAMA_NOSECURITY = "hogajama.nosecurity";

    private static final Logger LOG = LoggerFactory.getLogger(DetermineOwnerInterceptor.class);

    @Inject
    private OwnerService service;
    @Inject
    private UserContext userContext;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {
        SecurityContext sc = extractSecurityContext(ctx);
        
        String ssoUserId;
        if ( null == sc || null == sc.getUserPrincipal() ) {
            LOG.error("SecurityContext is null. This probably doesn't end well, since we need a configured user.");
            if ( "true".equals(System.getProperty(HOGAJAMA_NOSECURITY, "false")) ) {
                ssoUserId = "dummy";
                userContext.setUserProfile(getDummyUserProfile());
            }
            else {
                return ctx.proceed();
            }
        }
        else {
            ssoUserId = sc.getUserPrincipal().getName();
        }
        Optional<Owner> optionalOwner = service.getRegisteredOwner(ssoUserId);
        Owner owner = optionalOwner.orElseGet(() -> registerOwnerAndHandleDuplicates(ssoUserId));
        userContext.setOwner(owner);

        if (sc.getUserPrincipal() instanceof KeycloakPrincipal) {
            @SuppressWarnings("unchecked")
            KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc.getUserPrincipal();

            final AccessToken token = kp.getKeycloakSecurityContext().getToken();
            UserProfile userProfile = new UserProfile();
            userProfile.setName(token.getName());
            userProfile.setEmail(token.getEmail());
            userProfile.setFamilyName(token.getFamilyName());
            userProfile.setGivenName(token.getGivenName());
            userContext.setUserProfile(userProfile);
        } else {
            LOG.warn("System is not configured for Keycloak. Using login name as user id. Some information might be missing.");
        }

        return ctx.proceed();
    }

    private UserProfile getDummyUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setName("Dummy");
        userProfile.setEmail("dummy@nowhere");
        userProfile.setFamilyName("Dummy");
        userProfile.setGivenName("Franz");
        return userProfile;
    }

    private Owner registerOwnerAndHandleDuplicates(String ssoUserId) {
        try {
            return service.register(ssoUserId);
        } catch (Exception e) {
            if (e.getCause().getMessage().contains("ConstraintViolationException")) {
                LOG.warn("Tried to register owner twice.");
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
