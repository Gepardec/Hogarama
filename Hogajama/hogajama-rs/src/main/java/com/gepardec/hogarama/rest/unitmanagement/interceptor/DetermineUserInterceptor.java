package com.gepardec.hogarama.rest.unitmanagement.interceptor;


import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.domain.unitmanagement.service.UserService;
import com.gepardec.hogarama.security.UserProfileResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.Optional;

/**
 * Intercepts all requests annotated with {@link DetermineUser} and extracts logged in user
 * by request's bearer token. <br />
 * The extracted user will be stored in the {@link UserContext}.
 */
@DetermineUser
@Interceptor
public class DetermineUserInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(DetermineUserInterceptor.class);

    @Inject
    private UserService service;
    @Inject
    private UserContext userContext;
    @Inject
    private UserProfileResolver userProfileResolver;

    @AroundInvoke
    public Object aroundInvoke(InvocationContext ctx) throws Exception {

        UserProfile userProfile = userProfileResolver.resolveUserProfile();
        userContext.setUserProfile(userProfile);

        Optional<User> optionalUser = service.getRegisteredUser(userProfile.getEmail());
        User user = optionalUser.orElseGet(() -> registerUserAndHandleDuplicates(userProfile.getEmail()));
        userContext.getUser(user);

        return ctx.proceed();
    }

    private User registerUserAndHandleDuplicates(String userKey) {
        try {
            return service.register(userKey);
        } catch (Exception e) {
            if (e.getCause().getMessage().contains("ConstraintViolationException")) {
                LOG.warn("Tried to register user twice.");
                return service.getRegisteredUser(userKey).orElse(null);
            }
            throw e;
        }
    }
}
