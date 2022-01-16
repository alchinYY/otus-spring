package ru.otus.spring.library.service;

import java.util.List;

public interface EntityService<T> {

    T getById(int id);

    List<T> getAll();

    T save(T genre);

    T updateById(int id, T genre);

    void deleteById(int id);
}
