package ru.otus.spring.library.service;

public interface SequenceGeneratorService<T> {

    T generateSequence(String seqName);
}
