package com.ader.sockets.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepositorie<T> {
    // Optional<T> findById(Long id);
    List<T> findAll();
    void save(T entity);
    // void update(T entity);
    void delete(Long id);
}
