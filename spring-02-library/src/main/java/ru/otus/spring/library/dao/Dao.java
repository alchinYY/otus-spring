package ru.otus.spring.library.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<ID, T> {

    Optional<T> getById(ID id);

    void updateById(ID id, T entity);

    T save(T entity);

    List<T> getAll();

    void deleteById(ID id);

}
