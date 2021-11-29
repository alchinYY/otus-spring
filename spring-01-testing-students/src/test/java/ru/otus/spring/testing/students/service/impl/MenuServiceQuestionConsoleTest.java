package ru.otus.spring.testing.students.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;
import ru.otus.spring.testing.students.dao.QuestionDao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Сервис меню. Тестирования студентов")
@RunWith(MockitoJUnitRunner.class)
class MenuServiceQuestionConsoleTest {

    private static final long TEST_VALUE_MIN_CORRECT_ANSWER = 3;

    @Mock
    private QuestionDao questionDao;

    private MenuServiceQuestionConsole menuServiceQuestionConsole;

    @BeforeEach
    public void setup() {
        menuServiceQuestionConsole = new MenuServiceQuestionConsole(questionDao);
    }

    @Test
    @DisplayName("Тестирование проверки количества корректных ответов")
    void checkCorrectAnswers() {
        ReflectionTestUtils.setField(menuServiceQuestionConsole, "minCorrectAnswer", TEST_VALUE_MIN_CORRECT_ANSWER);
        assertThat(menuServiceQuestionConsole.checkCorrectAnswers(5)).isFalse();
        assertThat(menuServiceQuestionConsole.checkCorrectAnswers(2)).isTrue();
    }
}

