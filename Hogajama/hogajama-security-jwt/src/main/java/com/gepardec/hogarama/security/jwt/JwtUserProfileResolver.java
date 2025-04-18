package com.gepardec.hogarama.security.jwt;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.security.UserProfileResolver;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

/**
 * Implementation of UserProfileResolver that extracts user information from JWT tokens.
 *
 * <p>
 * This class extracts user profile information from a JSON Web Token (JWT) provided during
 * authentication. It uses the MicroProfile JWT API to access standardized claims from the token
 * and maps them to a UserProfile object.
 * </p>
 *
 * <p>
 * As the default implementation (annotated with @Default), this resolver is used in production
 * environments where JWT-based authentication is employed.
 * </p>
 */
@Default
public class JwtUserProfileResolver implements UserProfileResolver {

    /**
     * The JWT token from which user information will be extracted.
     */
    @Inject
    private JsonWebToken jwt;

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation extracts standard claims from the JWT token including:
     * <ul>
     *   <li>name: The user's full name</li>
     *   <li>email: The user's email address</li>
     *   <li>family_name: The user's last/family name</li>
     *   <li>given_name: The user's first/given name</li>
     * </ul>
     * </p>
     *
     * @return A UserProfile populated with information from the JWT
     */
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
