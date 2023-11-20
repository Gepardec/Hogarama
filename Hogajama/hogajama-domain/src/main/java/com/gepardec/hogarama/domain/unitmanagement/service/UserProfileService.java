package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;

import jakarta.inject.Inject;
import java.util.Optional;

public class UserProfileService {

    @Inject
    private UserContext userContext;

    public UserProfile getUserProfile(){
        return Optional.ofNullable(userContext.getUserProfile()).orElseThrow(() -> new RuntimeException("User Profile is not set"));
    }
}
