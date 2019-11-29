package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class BaseDao<T extends Serializable> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    public abstract Class<T> getEntityClass();

    @Override
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
    }

    @Override
    public List<T> findAll() {
        return entityManager.createQuery("from " + getEntityClass().getName())
                .getResultList();
    }

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public void deleteById(Long id) {
        Optional<T> optionalEntity = getById(id);
        optionalEntity.ifPresent(this::delete);
    }
}
