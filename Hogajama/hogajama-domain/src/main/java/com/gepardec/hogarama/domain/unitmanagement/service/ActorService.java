package com.gepardec.hogarama.domain.unitmanagement.service;

import com.gepardec.hogarama.domain.unitmanagement.context.UserContext;
import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

public class ActorService {

    @Inject
    private ActorDAO dao;

    @Inject
    private UserContext userContext;

    @Inject
    Event<Actor> actorChanged;

    public void createActor(Actor actor) {
        actor.verifyIsOwned(userContext.getUser());
        dao.save(actor);
    }

    public void deleteActor(Long actorId) {
        Actor actor = this.dao.getById(actorId)
                .orElseThrow(() -> new NotFoundException(String.format("Actor with id [%d] not found", actorId)));

        actorChanged.fire(actor);
        dao.delete(actor);
    }

    public void updateActor(Actor actor) {
        actor.verifyIsOwned(userContext.getUser());
        actorChanged.fire(actor);
        dao.update(actor);
    }

    public List<Actor> getAllActorsForUser() {
        return dao.getAllActorsForUser(userContext.getUser());
    }

}
