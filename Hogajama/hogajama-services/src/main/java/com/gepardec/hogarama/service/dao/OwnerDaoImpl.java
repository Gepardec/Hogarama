package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.entity.Owner;
import com.gepardec.hogarama.domain.owner.OwnerDao;

import javax.enterprise.context.Dependent;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Dependent
public class OwnerDaoImpl extends AbstractJpaDao<Owner> implements OwnerDao {

    @Override
    public Optional<Owner> getBySsoUserId(String ssoUserId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Owner> query = cb.createQuery(Owner.class);
        Root<Owner> root = query.from(Owner.class);
        // TODO use meta model for ssoUserId Owner_.ssoUserId
        query.select(root).where(cb.equal(root.get("ssoUserId"), ssoUserId));
        try {
            return Optional.of(
                    entityManager.createQuery(query).getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Class<Owner> getEntityClass() {
        return Owner.class;
    }
}
