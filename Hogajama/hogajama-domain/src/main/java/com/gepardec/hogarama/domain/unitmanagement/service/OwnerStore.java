package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class OwnerStore {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerStore.class);

    private Owner owner;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        LOG.debug("Set owner with id {} and ssoUserId {}.", owner.getId(), owner.getSsoUserId());
        this.owner = owner;
    }
}
