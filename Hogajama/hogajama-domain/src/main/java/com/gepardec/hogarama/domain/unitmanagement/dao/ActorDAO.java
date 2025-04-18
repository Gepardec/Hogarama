package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.Actor;
import com.gepardec.hogarama.domain.unitmanagement.entity.QActor;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Actor entities.
 *
 * <p>
 * This class extends BaseDAO to provide CRUD operations specific to Actor entities.
 * It also includes additional methods for retrieving actors based on specific criteria
 * such as user ownership and device identifier.
 * </p>
 *
 * <p>
 * The class uses QueryDSL for type-safe queries.
 * </p>
 */
@Dependent
public class ActorDAO extends BaseDAO<Actor> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<Actor> getEntityClass() {
        return Actor.class;
    }

    /**
     * Retrieves all actors owned by a specific user.
     *
     * <p>
     * This method finds all actors associated with units that belong to the specified user.
     * </p>
     *
     * @param user The user whose actors should be retrieved
     * @return A list of Actor entities owned by the user
     */
    public List<Actor> getAllActorsForUser(User user) {
        JPAQuery<Actor> query = new JPAQuery<>(entityManager);
        QActor actor = QActor.actor;
        return query.select(actor).from(actor).where(actor.unit.user.id.eq(user.getId())).fetch();
    }

    /**
     * Finds an actor by its device identifier.
     *
     * <p>
     * Device identifier is a unique string used to identify physical actor devices
     * in the system.
     * </p>
     *
     * @param id The device identifier to search for
     * @return An Optional containing the Actor if found, or empty if not found
     */
    public Optional<Actor> getByDeviceId(String id) {
        JPAQuery<Actor> query = new JPAQuery<>(entityManager);
        QActor actor = QActor.actor;
        return Optional.ofNullable(query.select(actor).from(actor).where(actor.deviceId.eq(id)).fetchOne());
    }
}
