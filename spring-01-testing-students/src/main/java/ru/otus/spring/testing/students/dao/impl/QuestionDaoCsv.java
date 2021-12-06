package ru.otus.spring.testing.students.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.spring.testing.students.dao.Dao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.util.csv.CsvReader;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuestionDaoCsv implements Dao<Question> {

    private final CsvReader<Question> questionCsvReader;


    @Override
    public List<Question> findAll() {
        return questionCsvReader.readData();
    }

    @Override
    public Question findById(int id) {
        return questionCsvReader.readData().get(id);
    }
}
