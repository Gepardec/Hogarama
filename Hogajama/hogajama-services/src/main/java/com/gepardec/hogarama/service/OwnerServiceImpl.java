package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.owner.OwnerDao;
import com.gepardec.hogarama.domain.owner.OwnerService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
public class OwnerServiceImpl implements OwnerService {

    @Inject
    private OwnerDao ownerDao;

    @Override
    public boolean isRegistered(String ssoUserId) {
        return ownerDao.getBySsoUserId(ssoUserId).isPresent();
    }

    @Transactional
    @Override
    public void register(String ssoUserId) {
        Owner owner = new Owner();
        owner.setSsoUserId(ssoUserId);
        ownerDao.save(owner);
//        TODO create default unit
    }
}
