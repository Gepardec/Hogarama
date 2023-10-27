package com.gepardec.hogarama.security;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;

import jakarta.ws.rs.core.SecurityContext;

public interface UserProfileResolver {
  UserProfile resolveUserProfile();
}
