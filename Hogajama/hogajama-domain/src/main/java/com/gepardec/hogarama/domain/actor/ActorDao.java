package com.gepardec.hogarama.domain.actor;

import com.gepardec.hogarama.domain.GenericDao;
import com.gepardec.hogarama.domain.entity.Actor;

import java.util.List;

public interface ActorDao extends GenericDao<Actor> {

    List<Actor> getAllActorForOwner(Long ownerId);
}
