package com.gepardec.hogarama.security.dummy;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.security.UserProfileResolver;

import javax.ws.rs.core.SecurityContext;

public class DummyUserProfileResolver implements UserProfileResolver {

  @Override
  public UserProfile resolveUserProfile(SecurityContext sc) {
    UserProfile userProfile = new UserProfile();
    userProfile.setName("Dummy");
    userProfile.setEmail("dummy@nowhere");
    userProfile.setFamilyName("Dummy");
    userProfile.setGivenName("Franz");
    return userProfile;
  }
}
