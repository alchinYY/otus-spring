package ru.otus.spring.testing.students.dao;

import java.util.List;

public interface Dao<T> {

    List<T> findAll();

    T findById(int id);

}