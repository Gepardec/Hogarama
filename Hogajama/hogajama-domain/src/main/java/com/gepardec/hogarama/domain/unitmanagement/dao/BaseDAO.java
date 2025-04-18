package com.gepardec.hogarama.domain.unitmanagement.dao;

import com.gepardec.hogarama.domain.unitmanagement.entity.EntityNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Base abstract class for Data Access Objects (DAOs) in the Hogarama system.
 *
 * <p>
 * This class provides common CRUD operations for entity classes that implement Serializable.
 * Specific entity DAOs should extend this class and implement the {@link #getEntityClass()} method
 * to provide type-specific functionality.
 * </p>
 *
 * <p>
 * The class uses JPA's EntityManager for database operations and provides methods for
 * retrieving, creating, updating, and deleting entities.
 * </p>
 *
 * @param <T> The entity type this DAO manages, must implement Serializable
 */
public abstract class BaseDAO<T extends Serializable> {

    /**
     * JPA EntityManager injected by the container.
     */
    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Returns the class object for the entity type managed by this DAO.
     *
     * @return The Class object for type T
     */
    public abstract Class<T> getEntityClass();

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The unique identifier of the entity
     * @return An Optional containing the entity if found, or empty if not found
     */
    public Optional<T> getById(Long id) {
        return Optional.ofNullable(entityManager.find(getEntityClass(), id));
    }

    /**
     * Retrieves an entity by its ID, throwing an exception if not found.
     *
     * @param id The unique identifier of the entity
     * @return The entity with the specified ID
     * @throws EntityNotFoundException if no entity with the given ID exists
     */
    public T getByIdNonOpt(Long id) {
        return getById(id).orElseThrow(() -> new EntityNotFoundException(id, getEntityClass()));
    }

    /**
     * Retrieves all entities of this type from the database.
     *
     * @return A list of all entities
     */
    public List<T> findAll() {
        return entityManager.createQuery("from " + getEntityClass().getName())
                .getResultList();
    }

    /**
     * Persists a new entity to the database.
     *
     * @param entity The entity to save
     */
    public void save(T entity) {
        entityManager.persist(entity);
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity The entity to update
     */
    public void update(T entity) {
        entityManager.merge(entity);
    }

    /**
     * Deletes an entity from the database.
     *
     * @param entity The entity to delete
     */
    public void delete(T entity) {
        entityManager.remove(entity);
    }

    /**
     * Deletes an entity by its ID if it exists.
     *
     * @param id The unique identifier of the entity to delete
     */
    public void deleteById(Long id) {
        Optional<T> optionalEntity = getById(id);
        optionalEntity.ifPresent(this::delete);
    }
}
