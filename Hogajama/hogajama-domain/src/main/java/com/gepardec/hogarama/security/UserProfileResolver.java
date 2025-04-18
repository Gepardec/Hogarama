package com.gepardec.hogarama.security;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;

/**
 * Interface for resolving user profile information.
 *
 * <p>
 * This interface defines the contract for components that extract user profile information
 * from authentication contexts. It allows different implementations to be used depending on
 * the authentication mechanism (e.g., JWT tokens, dummy authentication for testing).
 * </p>
 *
 * <p>
 * Implementations of this interface are responsible for obtaining user details such as name,
 * email address, and other profile information from the authentication source and converting
 * it to a standardized UserProfile object that can be used throughout the application.
 * </p>
 */
public interface UserProfileResolver {

    /**
     * Resolves and returns the profile information for the current user.
     *
     * <p>
     * This method extracts user information from the current authentication context
     * and creates a UserProfile object containing that information.
     * </p>
     *
     * @return A UserProfile object containing the current user's profile information
     */
    UserProfile resolveUserProfile();
}
