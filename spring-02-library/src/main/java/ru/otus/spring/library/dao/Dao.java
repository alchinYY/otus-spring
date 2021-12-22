package ru.otus.spring.library.dao;

import java.util.List;

public interface Dao<ID, T> {

    T getById(ID id);

    void updateById(ID id, T entity);

    ID save(T entity);

    List<T> getAll();

    void deleteById(ID id);

}
