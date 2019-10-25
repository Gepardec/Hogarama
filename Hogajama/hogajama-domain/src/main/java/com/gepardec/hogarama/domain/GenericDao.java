package com.gepardec.hogarama.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends Serializable> {
    Optional<T> getById(Long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    void deleteById(Long id);
}
