package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.dao.OwnerDAO;
import com.gepardec.hogarama.domain.unitmanagement.dao.UnitDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Owner;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

@RequestScoped
public class OwnerService {

    private static final Logger LOG = LoggerFactory.getLogger(OwnerService.class);

    @Inject
    private OwnerDAO ownerDao;
    @Inject
    private UnitDAO unitDao;

    public Optional<Owner> getRegisteredOwner(String ssoUserId) {
        return ownerDao.getBySsoUserId(ssoUserId);
    }

    @Transactional
    public Owner register(String ssoUserId) {
        LOG.info("Register new owner with ssoUserId {}.", ssoUserId);
        Owner owner = new Owner();
        owner.setSsoUserId(ssoUserId);
        ownerDao.save(owner);
        Unit defaultUnit = Unit.createDefault(owner);
        unitDao.save(defaultUnit);
        owner.addUnit(defaultUnit);
        return owner;
    }

}
