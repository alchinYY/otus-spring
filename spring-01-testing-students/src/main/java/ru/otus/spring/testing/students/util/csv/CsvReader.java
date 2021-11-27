package ru.otus.spring.testing.students.util.csv;

import java.util.List;

public interface CsvReader<T> {
    List<T> readData();
}
