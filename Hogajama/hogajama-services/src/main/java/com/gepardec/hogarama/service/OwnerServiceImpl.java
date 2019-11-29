package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.owner.OwnerDao;
import com.gepardec.hogarama.domain.owner.OwnerService;
import com.gepardec.hogarama.domain.unit.UnitDao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@RequestScoped
public class OwnerServiceImpl implements OwnerService {

    @Inject
    private OwnerDao ownerDao;
    @Inject
    private UnitDao unitDao;

    @Override
    public Optional<Owner> getRegisteredOwner(String ssoUserId) {
        return ownerDao.getBySsoUserId(ssoUserId);
    }

    @Transactional
    @Override
    public Owner register(String ssoUserId) {
        Owner owner = new Owner();
        owner.setSsoUserId(ssoUserId);
        try {
            ownerDao.save(owner);
        } catch (Exception e) {
            return getRegisteredOwner(ssoUserId).orElse(null);
        }
        unitDao.save(Unit.createDefault(owner));
        return owner;
    }

}
