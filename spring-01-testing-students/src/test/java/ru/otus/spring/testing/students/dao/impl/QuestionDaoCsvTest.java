package ru.otus.spring.testing.students.dao.impl;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;
import ru.otus.spring.testing.students.util.csv.CsvReader;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("DAO. Read data from hard-csv file")
@ExtendWith(MockitoExtension.class)
class QuestionDaoCsvTest {

    @Mock
    private CsvReader<Question> questionCsvReader;

    @InjectMocks
    private QuestionDaoCsv questionDaoCsv;
    
    @Test
    @DisplayName("find all test")
    void findAll() {
        List<Question> questionList = Lists.newArrayList(mock(Question.class), mock(Question.class));

        when(questionCsvReader.readData()).thenReturn(questionList);
        List<Question> questionListActual = questionDaoCsv.findAll();

        assertEquals(questionList, questionListActual);
        verify(questionCsvReader, times(1)).readData();
    }

    @Test
    void getById() {
        Question question = new Question(
                1,
                "Where do we study?",
                QuestionType.TEXT,
                "otus",
                null,
                null
        );

        when(questionCsvReader.readData()).thenReturn(Collections.singletonList(question));
        assertEquals(question, questionDaoCsv.findById(0));
    }
}