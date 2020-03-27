package org.dcm4che.test.support;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.function.Supplier;

public class JPASupportImpl implements JPASupport {
	
	@PersistenceContext
	EntityManager em;
	
	@Inject
	UserTransaction tx;

	private static Mapper mapper = new DozerBeanMapper();

	@Override
	public void beginTx() {
		try {
			tx.begin();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void commitTx() {
		try {
			tx.commit();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> T unwrap(T jpaEntity, Class<T> clazz) {
		return mapper.map(jpaEntity, clazz);
	}
}
