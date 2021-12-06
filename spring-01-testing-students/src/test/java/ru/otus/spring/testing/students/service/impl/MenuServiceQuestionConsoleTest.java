package ru.otus.spring.testing.students.service.impl;

import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import ru.otus.spring.testing.students.config.LocalizationConfig;
import ru.otus.spring.testing.students.dao.Dao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;
import ru.otus.spring.testing.students.service.InOutService;
import ru.otus.spring.testing.students.service.L10nMessageService;

import java.util.Arrays;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.otus.spring.testing.students.service.impl.MenuServiceQuestionConsole.MESSAGE_CONGRATULATIONS;
import static ru.otus.spring.testing.students.service.impl.MenuServiceQuestionConsole.MESSAGE_TEST_FAIL;

@DisplayName("Сервис меню. Тестирования студентов")
@ExtendWith(MockitoExtension.class)
class MenuServiceQuestionConsoleTest {

    private static final long TEST_VALUE_MIN_CORRECT_ANSWER = 2;
    private static final String EN_LOCALE = "en_EN";
    private static final String RU_LOCALE = "ru_RU";

    private static Question txtQuestion;

    private static Question testQuestion;

    @Mock
    private Dao<Question> questionDao;

    @Mock
    private L10nMessageService l10nMessageService;

    @Mock
    private LocalizationConfig localizationConfig;

    @Mock
    private InOutService inOutService;

    @InjectMocks
    private MenuServiceQuestionConsole menuServiceQuestionConsole;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(menuServiceQuestionConsole, "minCorrectAnswer", TEST_VALUE_MIN_CORRECT_ANSWER);
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
    @DisplayName("Тестирование проверки количества корректных ответов")
    void checkCorrectAnswers() {
        assertThat(menuServiceQuestionConsole.checkCorrectAnswers(5)).isFalse();
        assertThat(menuServiceQuestionConsole.checkCorrectAnswers(0)).isTrue();
    }

    @Test
    void createOneSession_setDefaultLocale() {
        doNothing().when(inOutService).print(anyString());
        when(localizationConfig.getDefaultTag()).thenReturn(RU_LOCALE);

        menuServiceQuestionConsole.createOneSession();

        verify(inOutService, times(1)).print(any());
        verify(inOutService, times(3)).println(any());
    }

    @Test
    void createOneSession_setCustomLocale() {
        doNothing().when(inOutService).print(anyString());
        when(localizationConfig.getDefaultTag()).thenReturn(RU_LOCALE);

        when(localizationConfig.getLocalMapping())
                .thenReturn(Maps.newHashMap("1", EN_LOCALE));

        when(inOutService.read()).thenReturn("1");
        menuServiceQuestionConsole.createOneSession();

        assertThat(Locale.getDefault()).isEqualTo(Locale.forLanguageTag(EN_LOCALE));

        verify(inOutService, times(1)).print(any());
        verify(inOutService, times(4)).println(any());
        verify(inOutService, times(1)).read();
    }

    @Test
    void createOneSession_withCorrectAnswer() {
        doNothing().when(inOutService).print(any());
        doNothing().when(inOutService).println(any());

        when(localizationConfig.getDefaultTag()).thenReturn(RU_LOCALE);
        when(inOutService.read()).thenReturn("1", "test", "1");


        when(questionDao.findAll()).thenReturn(
                Arrays.asList(txtQuestion, testQuestion)
        );

        menuServiceQuestionConsole.createOneSession();

        assertThat(Locale.getDefault()).isEqualTo(Locale.forLanguageTag(EN_LOCALE));
        verify(inOutService, times(3)).print(any());
        verify(inOutService, times(9)).println(any());
        verify(inOutService, times(3)).read();
        verify(l10nMessageService, times(7)).getMessage(anyString());
        verify(l10nMessageService, times(1)).getMessage(MESSAGE_CONGRATULATIONS);
    }

    @Test
    void createOneSession_withNotCorrectAnswer() {
        doNothing().when(inOutService).print(any());
        doNothing().when(inOutService).println(any());
        when(localizationConfig.getDefaultTag()).thenReturn(RU_LOCALE);
        when(inOutService.read()).thenReturn("1", "_", "2");
        when(questionDao.findAll()).thenReturn(
                Arrays.asList(txtQuestion, testQuestion)
        );

        menuServiceQuestionConsole.createOneSession();

        assertThat(Locale.getDefault()).isEqualTo(Locale.forLanguageTag(EN_LOCALE));
        verify(inOutService, times(3)).print(any());
        verify(inOutService, times(9)).println(any());
        verify(inOutService, times(3)).read();
        verify(l10nMessageService, times(7)).getMessage(anyString());
        verify(l10nMessageService, times(1)).getMessage(MESSAGE_TEST_FAIL);
    }
}
