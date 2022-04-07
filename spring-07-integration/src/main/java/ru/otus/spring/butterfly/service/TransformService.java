package ru.otus.spring.butterfly.service;

public interface TransformService<F, T> {

    T transform(F from);

}
