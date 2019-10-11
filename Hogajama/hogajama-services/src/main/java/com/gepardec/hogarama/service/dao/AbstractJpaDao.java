package com.gepardec.hogarama.service.dao;

import com.gepardec.hogarama.domain.GenericDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class AbstractJpaDao<T extends Serializable> implements GenericDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    public abstract Class<T> getEntityClass();

    @Override
    public T getById(Long id) {
        return entityManager.find(getEntityClass(), id);
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
        T entity = getById(id);
        delete(entity);
    }
}
