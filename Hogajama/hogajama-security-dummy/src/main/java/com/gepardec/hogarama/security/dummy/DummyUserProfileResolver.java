package com.gepardec.hogarama.security.dummy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.security.UserProfileResolver;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.Optional;

/**
 * Implementation of UserProfileResolver for testing and development environments.
 *
 * <p>
 * This class provides a simple mechanism for specifying user profile information
 * in development and testing environments without requiring a full authentication system.
 * It can either use default dummy values or extract user information from a special
 * "Dummy" authorization header.
 * </p>
 *
 * <p>
 * The Dummy authorization header should contain a Base64-encoded JSON representation
 * of a UserProfile object. If this header is not present or cannot be parsed,
 * default dummy values are used instead.
 * </p>
 */
public class DummyUserProfileResolver implements UserProfileResolver {

    /**
     * The HTTP request from which authorization information will be extracted.
     */
    @Inject
    private HttpServletRequest request;

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation first attempts to extract user profile information from
     * a "Dummy" authorization header. If that fails, it creates a user profile with
     * default values:
     * <ul>
     *   <li>Name: "Dummy"</li>
     *   <li>Email: "dummy@nowhere"</li>
     *   <li>Family Name: "Dummy"</li>
     *   <li>Given Name: "Franz"</li>
     * </ul>
     * </p>
     *
     * @return A UserProfile populated either from the authorization header or with default values
     */
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

    /**
     * Extracts user profile information from the "Dummy" authorization header.
     *
     * <p>
     * This method looks for an Authorization header in the format "Dummy [base64-encoded-json]",
     * decodes the Base64 content, and parses it as a JSON representation of a UserProfile.
     * </p>
     *
     * @return The extracted UserProfile, or null if the header was not present or could not be parsed
     */
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
