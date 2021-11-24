package ru.otus.spring.testing.students.dao;

import ru.otus.spring.testing.students.domain.Question;

import java.util.List;

public interface QuestionDao {

    List<Question> findAll();

    Question getById(int id);

}
