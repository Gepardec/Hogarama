package org.dcm4che.test.support;

import java.util.function.Supplier;

public interface JPASupport {
	public void beginTx();
	public void commitTx();
	public <T> T unwrap(T jpaEntity, Class<T> clazz);
}
