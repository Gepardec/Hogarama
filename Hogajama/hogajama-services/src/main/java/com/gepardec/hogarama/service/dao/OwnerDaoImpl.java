package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.QOwner;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.gepardec.hogarama.domain.owner.OwnerDao;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.Optional;

@Dependent
public class OwnerDaoImpl extends BaseDao<Owner> implements OwnerDao {

    @Override
    public Optional<Owner> getBySsoUserId(String ssoUserId) {
        QOwner owner = QOwner.owner;
        JPAQuery<Owner> query = new JPAQuery<>(entityManager);
        try {
            Owner result = query.select(owner).from(owner).where(owner.ssoUserId.eq(ssoUserId)).fetchOne();
            return Optional.ofNullable(result);
        } catch (NonUniqueResultException e) {
            throw new TechnicalException("Multiple results for owner with ssoUserId " + ssoUserId + " found.", e);
        }
    }

    @Override
    public Class<Owner> getEntityClass() {
        return Owner.class;
    }
}
