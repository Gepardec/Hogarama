package org.dcm4che.test.em;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

public class DBAssistImpl implements DBAssist {
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	UserTransaction tx;

	@Override
	public <T> T load(Object id, Class<T> clazz) {
		return em.find(clazz, id);
	}

	@Override
	public <T> T update(T entity) {
		try {
			tx.begin();
			T merged = em.merge(entity);
			tx.commit();
			return merged;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T insert(T entity) {
		
		try {
			tx.begin();
			em.persist(entity);
			tx.commit();
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Object entity) {

		try {
			tx.begin();
			entity = em.merge(entity);
			em.remove(entity);
			tx.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T query(String query, Class<T> clazz) {
		return em.createQuery(query, clazz).getSingleResult();
	}

    
}
