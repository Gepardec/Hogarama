package com.gepardec.hogarama.service;

import com.gepardec.hogarama.domain.actor.ActorDao;
import com.gepardec.hogarama.domain.entity.Actor;
import com.gepardec.hogarama.domain.entity.Unit;
import com.gepardec.hogarama.domain.exception.TechnicalException;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class ActorService {

    @Inject
    private ActorDao dao;
    @Inject
    private OwnerStore store;

    public void createActor(Actor actor) {
        if (store.getOwner().getUnitList().stream().map(Unit::getId).collect(Collectors.toSet()).contains(actor.getUnit().getId())) {
            dao.save(actor);
        } else {
            throw new TechnicalException("Unit doesn't belong to owner");
        }
    }
}
