package org.dcm4che.test.em;

public interface DBAssist {

	public <T> T load(Object id, Class<T> clazz);
	public <T> T query(String query, Class<T> clazz);
	public <T> T update(T entity);
	public <T> T insert(T entity);
	public void remove(Object entity);
	public <T> void removeById(Object id, Class<T> clazz);
    
}
