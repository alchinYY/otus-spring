package ru.otus.spring.library.service;

import java.util.List;

public interface EntityService<T> {

    T getById(long id);

    List<T> getAll();

    T save(T genre);

    T updateById(Long id, T genre);

    void deleteById(Long id);
}
