package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UnitManagementContext;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;

import javax.inject.Inject;
import java.util.Optional;

public class UserProfileService {

    @Inject
    private UnitManagementContext context;

    public UserProfile getUserProfile(){
        return Optional.ofNullable(context.getUserProfile()).orElseThrow(() -> new RuntimeException("User Profile is not set"));
    }
}
