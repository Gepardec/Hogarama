package com.gepardec.hogarama.domain.unitmanagement.cache;

import com.gepardec.hogarama.domain.unitmanagement.dao.ActorDAO;
import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ActorCache {

    private static final Logger LOG = LoggerFactory.getLogger(ActorCache.class);

    @Inject
    private ActorDAO dao;

    private Map<String, Actor> cache = new HashMap<>();

    public void invalidateCache(@Observes Actor actor) {
        LOG.info("Invalidate actor cache.");
        cache = new HashMap<>();
    }

    public Optional<Actor> getByDeviceId(String deviceId) {
        Optional<Actor> actor = getCachedActor(deviceId);
        if ( actor.isPresent()) {
            return actor;
        }
        return loadActor(deviceId);
    }

    private synchronized Optional<Actor> loadActor(String deviceId) {
        Optional<Actor> optionalActor = getCachedActor(deviceId);
        if (optionalActor.isPresent()) {
            return optionalActor;
        }

        LOG.info("Actor not found in cache, read from database with deviceId: " + deviceId);
        optionalActor = dao.getByDeviceId(deviceId);
        if (optionalActor.isPresent()) {
            Actor actor = optionalActor.get();
            LOG.info("Actor {} found. UnitName: {}", actor.getName(), actor.getUnit().getName());
            cache.put(deviceId, optionalActor.get());
        }
        return optionalActor;
    }

    private Optional<Actor> getCachedActor(String deviceId) {
        return Optional.ofNullable(cache.get(deviceId));
    }

}
