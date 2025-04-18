package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.entity.QUser;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.enterprise.context.Dependent;

import java.util.Optional;

/**
 * Data Access Object for User entities.
 *
 * <p>
 * This class extends BaseDAO to provide CRUD operations specific to User entities.
 * It also includes additional methods for retrieving users based on specific criteria
 * such as the user key (typically mapped to an external identity provider ID).
 * </p>
 *
 * <p>
 * The class uses QueryDSL for type-safe queries.
 * </p>
 */
@Dependent
public class UserDAO extends BaseDAO<User> {

    /**
     * Finds a user by their unique key.
     *
     * <p>
     * The user key is typically an external identifier from an identity provider
     * (such as a Keycloak or OAuth user ID) that uniquely identifies a user.
     * </p>
     *
     * @param userKey The unique key to search for
     * @return An Optional containing the User if found, or empty if not found
     * @throws TechnicalException if multiple users are found with the same key (which should never happen)
     */
    public Optional<User> getByKey(String userKey) {
        QUser user = QUser.user;
        JPAQuery<User> query = new JPAQuery<>(entityManager);
        try {
            User result = query.select(user).from(user).where(user.key.eq(userKey)).fetchOne();
            return Optional.ofNullable(result);
        } catch (NonUniqueResultException e) {
            throw new TechnicalException("Multiple results for user with key " + userKey + " found.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
