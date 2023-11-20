package com.gepardec.hogarama.domain.unitmanagement.context;

import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UserContext {

    private static final Logger LOG = LoggerFactory.getLogger(UserContext.class);

    private User user;
    private UserProfile userProfile;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        LOG.debug("Set user with id {} and key {}.", user.getId(), user.getKey());
        this.user = user;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
