package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.owner.OwnerDao;
import com.gepardec.hogarama.domain.unit.UnitDao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@RequestScoped
public class OwnerService {

    @Inject
    private OwnerDao ownerDao;
    @Inject
    private UnitDao unitDao;

    public Optional<Owner> getRegisteredOwner(String ssoUserId) {
        return ownerDao.getBySsoUserId(ssoUserId);
    }

    @Transactional
    public Owner register(String ssoUserId) {
        Owner owner = new Owner();
        owner.setSsoUserId(ssoUserId);
        ownerDao.save(owner);
        Unit defaultUnit = Unit.createDefault(owner);
        unitDao.save(defaultUnit);
        owner.addToUnitList(defaultUnit);
        return owner;
    }

}
