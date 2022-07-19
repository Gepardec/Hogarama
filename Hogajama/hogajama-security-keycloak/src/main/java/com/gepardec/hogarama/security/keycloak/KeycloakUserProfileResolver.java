package com.gepardec.hogarama.security.keycloak;

import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import com.gepardec.hogarama.security.UserProfileResolver;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.SecurityContext;

public class KeycloakUserProfileResolver implements UserProfileResolver {

  private static final Logger LOG = LoggerFactory.getLogger(KeycloakUserProfileResolver.class);

  @Override
  public UserProfile resolveUserProfile(SecurityContext sc) {
    UserProfile userProfile = new UserProfile();

    if (sc.getUserPrincipal() instanceof KeycloakPrincipal) {
      @SuppressWarnings("unchecked")
      KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) sc.getUserPrincipal();

      final AccessToken token = kp.getKeycloakSecurityContext().getToken();
      userProfile.setName(token.getName());
      userProfile.setEmail(token.getEmail());
      userProfile.setFamilyName(token.getFamilyName());
      userProfile.setGivenName(token.getGivenName());
    } else {
      LOG.warn("System is not configured for Keycloak. Using login name as user id. Some information might be missing.");
    }

    return userProfile;
  }
}
