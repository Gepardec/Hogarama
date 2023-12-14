package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.unitmanagement.entity.User;
import com.gepardec.hogarama.domain.unitmanagement.entity.QUser;
import com.gepardec.hogarama.domain.unitmanagement.entity.Unit;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;

import jakarta.enterprise.context.Dependent;

import java.util.List;
import java.util.Optional;

@Dependent
public class UserDAO extends BaseDAO<User> {

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

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}
