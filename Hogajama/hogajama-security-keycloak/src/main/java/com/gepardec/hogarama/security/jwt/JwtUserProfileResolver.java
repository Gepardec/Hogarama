package com.gepardec.hogarama.security.jwt;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.security.UserProfileResolver;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;


import javax.inject.Inject;

public class JwtUserProfileResolver implements UserProfileResolver {

    @Inject
    private JsonWebToken jwt;

    @Override
    public UserProfile resolveUserProfile() {
        UserProfile userProfile = new UserProfile();

        userProfile.setName(jwt.getClaim("name"));
        userProfile.setEmail(jwt.getClaim(Claims.email));
        userProfile.setFamilyName(jwt.getClaim(Claims.family_name));
        userProfile.setGivenName(jwt.getClaim(Claims.given_name));

        return userProfile;
    }
}
