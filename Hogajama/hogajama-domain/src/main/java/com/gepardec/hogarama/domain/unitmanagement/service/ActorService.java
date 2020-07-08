package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class ActorService {

    @Inject
    private ActorDAO dao;

    @Inject
    private UserContext userContext;

    public void createActor(Actor actor) {
        actor.verifyIsOwned(userContext.getOwner());
        dao.save(actor);
    }

    public void deleteActor(Long actorId) {
        Actor actor = this.dao.getById(actorId)
                .orElseThrow(() -> new NotFoundException(String.format("Actor with id [%d] not found", actorId)));

        dao.delete(actor);
    }

    public void updateActor(Actor actor) {
        actor.verifyIsOwned(userContext.getOwner());
        dao.update(actor);
    }

    public List<Actor> getAllActorsForOwner() {
        return dao.getAllActorsForOwner(userContext.getOwner());
    }

}
