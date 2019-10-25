package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class OwnerStore {

    private Owner owner;

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
