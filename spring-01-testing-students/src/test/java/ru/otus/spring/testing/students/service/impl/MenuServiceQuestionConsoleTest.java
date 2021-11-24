package ru.otus.spring.testing.students.service.impl;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.otus.spring.testing.students.dao.QuestionDao;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Сервис меню. Тестирования студентов")
@RunWith(MockitoJUnitRunner.class)
class MenuServiceQuestionConsoleTest {

    private static final long TEST_VALUE_MIN_CORRECT_ANSWER = 3;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private MenuServiceQuestionConsole menuServiceQuestionConsole;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(menuServiceQuestionConsole, "minCorrectAnswer", TEST_VALUE_MIN_CORRECT_ANSWER);
    }

    @Test
    @DisplayName("Тестирование проверки количества корректных ответов")
    void checkCorrectAnswers() {
        assertFalse(menuServiceQuestionConsole.checkCorrectAnswers(5));
        assertTrue(menuServiceQuestionConsole.checkCorrectAnswers(2));
    }
}

