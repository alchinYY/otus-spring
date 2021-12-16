package ru.otus.spring.testing.students.dao;

public interface DaoSimple<T> {

    T get();

    void create(T t);
}
