package com.gepardec.hogarama.domain.unitmanagement.context;

import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UnitManagementContext {

    private static final Logger LOG = LoggerFactory.getLogger(UnitManagementContext.class);

    private Owner owner;
    private UserProfile userProfile;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        LOG.debug("Set owner with id {} and ssoUserId {}.", owner.getId(), owner.getSsoUserId());
        this.owner = owner;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
