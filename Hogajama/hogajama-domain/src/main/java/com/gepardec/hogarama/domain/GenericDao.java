package com.gepardec.hogarama.domain;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Serializable> {
    T getById(Long id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    void deleteById(Long id);
}
