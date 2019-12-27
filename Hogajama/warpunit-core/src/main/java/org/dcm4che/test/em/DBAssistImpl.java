package org.dcm4che.test.em;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

public class DBAssistImpl implements DBAssist {

	private static Mapper mapper = new DozerBeanMapper();
	
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
			T result = (T) mapper.map(merged, entity.getClass());
			tx.commit();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T insert(T entity) {
		
		try {
			tx.begin();
			em.persist(entity);
			T result = (T) mapper.map(entity, entity.getClass());
			tx.commit();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Object entity) {

		try {
			tx.begin();
			Object toBeRemoved = em.merge(entity);
			em.remove(toBeRemoved);
			tx.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> void removeById(Object id, Class<T> clazz) {
		try {
			tx.begin();
			Object toBeRemoved = em.find(clazz, id);
			em.remove(toBeRemoved);
			tx.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T query(String query, Class<T> clazz) {
		try {
			tx.begin();
			T entity = em.createQuery(query, clazz).getSingleResult();
			T result = (T) mapper.map(entity, entity.getClass());
			tx.commit();
			return result;
		} catch (Exception e) {
			throw  new RuntimeException(e);
		}
	}

    
}
