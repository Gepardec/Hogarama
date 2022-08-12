package com.gepardec.hogarama.security.dummy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.security.UserProfileResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Optional;

public class DummyUserProfileResolver implements UserProfileResolver {

    @Inject
    private HttpServletRequest request;

    @Override
    public UserProfile resolveUserProfile() {
        UserProfile userProfile = Optional.ofNullable(extractFromRequest()).orElse(new UserProfile());
        if (userProfile.getName() == null) {
            userProfile.setName("Dummy");
        }

        if (userProfile.getEmail() == null) {
            userProfile.setEmail("dummy@nowhere");
        }

        if (userProfile.getFamilyName() == null) {
            userProfile.setFamilyName("Dummy");
        }

        if (userProfile.getGivenName() == null) {
            userProfile.setGivenName("Franz");
        }

        return userProfile;
    }

    private UserProfile extractFromRequest() {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.contains("Dummy ")) {
            return null;
        }

        String json = new String(Base64.getDecoder().decode(authorization.split("\\s")[1]));

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, UserProfile.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
