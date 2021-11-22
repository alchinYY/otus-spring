package ru.otus.spring.testing.students.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DAO. Read data from hard-csv file")
@RunWith(MockitoJUnitRunner.class)
class QuestionDaoCsvTest {

    private final static int CSV_DATA_RECORDS_SIZE = 5;

    @InjectMocks
    private QuestionDaoCsv questionDaoCsv;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(questionDaoCsv, "csvFileWithData", "/test.csv");
        ReflectionTestUtils.invokeMethod(questionDaoCsv, "readDataFile");
    }

    @Test
    @DisplayName("find all test")
    void findAll() {
        List<Question> questionList = questionDaoCsv.findAll();
        assertEquals(CSV_DATA_RECORDS_SIZE, questionList.size());
    }

    @Test
    void getById() {
        Question question = new Question(
                1, "Где мы обучаемся?", QuestionType.TEXT, "otus", Collections.emptyList(), null
        );
        assertEquals(question, questionDaoCsv.getById(0));

    }
}