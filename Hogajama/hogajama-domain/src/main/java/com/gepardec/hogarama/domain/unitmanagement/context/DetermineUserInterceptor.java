package com.gepardec.hogarama.domain.unitmanagement.context;


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

        User user = service.getRegisteredUser(userProfile.getEmail())
                .orElseGet(() -> synchronizedRegisterUser(userProfile.getEmail()));
        userContext.setUser(user);

        return ctx.proceed();
    }

    private User synchronizedRegisterUser(String userKey) {
        synchronized (UserService.class) {
            LOG.debug("In synchronizedRegisterUser");
            try {
                return service.getOrRegister(userKey);
            } finally {
                LOG.debug("Out of synchronizedRegisterUser");
            }
        }
    }
}
