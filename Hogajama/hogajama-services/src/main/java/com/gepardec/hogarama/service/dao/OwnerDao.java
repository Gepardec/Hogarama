package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.entity.QOwner;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.jpa.impl.JPAQuery;

import javax.enterprise.context.Dependent;
import java.util.Optional;

@Dependent
public class OwnerDao extends BaseDao<Owner> {
    private static QOwner ownerRef = QOwner.owner;

    public Optional<Owner> getBySsoUserId(String ssoUserId) {
        JPAQuery<Owner> query = new JPAQuery<>(entityManager);
        try {
            Owner result = query.select(ownerRef)
                    .from(ownerRef)
                    .where(ownerRef.ssoUserId.eq(ssoUserId))
                    .fetchOne();
            return Optional.ofNullable(result);
        } catch (NonUniqueResultException e) {
            throw new TechnicalException("Multiple results for owner with ssoUserId " + ssoUserId + " found.", e);
        }
    }

    @Override
    public Class<Owner> getEntityClass() {
        return Owner.class;
    }

    public Owner getOwnerOfCode(String oneTimeUseCode) {
        JPAQuery<Owner> query = new JPAQuery<>(entityManager);

        return query.select(ownerRef)
                .from(ownerRef)
                .where(ownerRef.currentHardwareRegisterCode.eq(oneTimeUseCode))
                .fetchOne();
    }
}
