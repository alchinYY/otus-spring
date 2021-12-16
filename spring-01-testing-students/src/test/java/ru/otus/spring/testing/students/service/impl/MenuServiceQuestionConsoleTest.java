package ru.otus.spring.testing.students.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import ru.otus.spring.testing.students.dao.Dao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;
import ru.otus.spring.testing.students.service.InOutService;
import ru.otus.spring.testing.students.service.L10nMessageService;
import ru.otus.spring.testing.students.service.MenuService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.otus.spring.testing.students.config.MessageBundle.MESSAGE_CONGRATULATIONS;
import static ru.otus.spring.testing.students.config.MessageBundle.MESSAGE_TEST_FAIL;

@DisplayName("Сервис меню. Тестирования студентов")
@SpringBootTest(properties = "min.correct.answer=2")
class MenuServiceQuestionConsoleTest {

    private static Question txtQuestion;
    private static Question testQuestion;

    @MockBean
    private Dao<Question> questionDao;

    @MockBean
    private L10nMessageService l10nMessageService;

    @MockBean
    private InOutService inOutService;

    @Autowired
    private MenuService menuServiceQuestionConsole;

    @ComponentScan("ru.otus.spring.testing.students.service.impl")
    @Configuration
    static class TestContextConfiguration {
    }

    @BeforeEach
    public void setup() {
        txtQuestion = new Question(
                1,
                "question.txt",
                QuestionType.TEXT,
                "test",
                null,
                null
        );

        testQuestion = new Question(
                1,
                "question.txt",
                QuestionType.TEXT,
                "1",
                Arrays.asList("1", "2"),
                null
        );
    }

    @Test
    void createOneSession_withCorrectAnswer() {
        doNothing().when(inOutService).print(any());
        doNothing().when(inOutService).println(any());

        when(inOutService.read()).thenReturn( "test", "1");


        when(questionDao.findAll()).thenReturn(
                Arrays.asList(txtQuestion, testQuestion)
        );
        menuServiceQuestionConsole.createOneSession();

        verify(inOutService, times(2)).print(any());
        verify(inOutService, times(8)).println(any());
        verify(inOutService, times(2)).read();
        verify(l10nMessageService, times(6)).getMessage(anyString());
        verify(l10nMessageService, times(1)).getMessage(MESSAGE_CONGRATULATIONS);
    }

    @Test
    void createOneSession_withNotCorrectAnswer() {
        doNothing().when(inOutService).print(any());
        doNothing().when(inOutService).println(any());
        when(inOutService.read()).thenReturn("_", "2");
        when(questionDao.findAll()).thenReturn(
                Arrays.asList(txtQuestion, testQuestion)
        );

        menuServiceQuestionConsole.createOneSession();

        verify(inOutService, times(2)).print(any());
        verify(inOutService, times(8)).println(any());
        verify(inOutService, times(2)).read();
        verify(l10nMessageService, times(6)).getMessage(anyString());
        verify(l10nMessageService, times(1)).getMessage(MESSAGE_TEST_FAIL);
    }
}
