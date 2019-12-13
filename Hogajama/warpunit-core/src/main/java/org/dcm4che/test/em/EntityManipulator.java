package org.dcm4che.test.em;

import org.dcm4che.test.remote.WarpUnit;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.io.Closeable;
import java.io.IOException;
import java.util.*;

/**
 * Allows to insert/modify temporary testdata in the database. The inserted/modified entities gets removed/reverted afterwards by calling the method {@link #close()} which
 * is derived from {@link Closeable}. This class can easily used as try-with-resource Statement:
 * 
 * <pre>{@code
        try (EntityManipulator inserter = new EntityManipulator()) {
            inserter.insertEntity(entity);
            // Test implementation
        } // entity is removed automatically 
 * </pre>
 * 
 * @author brucwe
 */
public class EntityManipulator implements Closeable {

	private static Mapper mapper = new DozerBeanMapper();

    private final Map<Object, Object> loadedEntities = new HashMap<>();
    private final Collection<Object> createdEntities = new ArrayList<>();
    private final Collection<Object> updatedEntities = new ArrayList<>();

    private DBAssist dbAssist;

    public EntityManipulator(String baseRestUrl) {
        dbAssist = WarpUnit.builder()
            .primaryClass(DBAssistImpl.class)
            .includeInterface(true)
            .url(baseRestUrl)
            .createProxyGate(DBAssist.class);
    }

    public <T> T loadEntity(Object id, Class<T> clazz) {
        T originalEntity = dbAssist.load(id, clazz);
        T copy = mapper.map(originalEntity, clazz);
        loadedEntities.put(copy, originalEntity);
        return copy;
    }
    
    public <T> T loadEntityByQuery(String query, Class<T> clazz) {
        T originalEntity = dbAssist.query(query, clazz);
        T copy = mapper.map(originalEntity, clazz);
        loadedEntities.put(copy, originalEntity);
        return copy;
    }

    public void updateEntity(Object entity) throws Exception {
        Object originalEntity = loadedEntities.get(entity);
        if (originalEntity == null) {
            throw new Exception("Entity must be loaded using method loadEntity");
        }
        updatedEntities.add(originalEntity);
        dbAssist.update(entity);
    }

    public List<Object> insertEntity(Object... entitiesToInsert) {
        List<Object> insertedEntities = new ArrayList<>();
        for (Object entity : entitiesToInsert) {
        	Object insertedEntity = dbAssist.insert(entity);
            insertedEntities.add(insertedEntity);
        }

        createdEntities.addAll(insertedEntities);
        return insertedEntities;
    }

    @Override
    public void close() throws IOException {
        Exception exception = null;
        exception = restoreUpdatedEntities(exception);
        exception = restoreCreatedEntities(exception);
        if (exception != null) {
            throw new RuntimeException(exception);
        }
    }

    private Exception restoreCreatedEntities(Exception exception) {
        for (Object entity : createdEntities) {
            try {
                dbAssist.remove(entity);
            } catch (Exception e) {
                // try to remove the rest
                exception = e;
            }
        }
        createdEntities.clear();
        return exception;
    }

    private Exception restoreUpdatedEntities(Exception exception) {
        for (Object entity : updatedEntities) {
            try {
                // restore updated entities
                dbAssist.update(entity);
            } catch (Exception e) {
                // try to remove the rest
                exception = e;
            }
        }
        updatedEntities.clear();
        loadedEntities.clear();
        return exception;
    }
}
