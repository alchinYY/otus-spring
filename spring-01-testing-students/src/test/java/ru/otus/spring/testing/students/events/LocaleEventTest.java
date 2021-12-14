package ru.otus.spring.testing.students.events;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.testing.students.config.LocalizationConfig;
import ru.otus.spring.testing.students.service.L10nMessageService;

import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static ru.otus.spring.testing.students.config.MessageBundle.*;

@SpringBootTest(classes = {LocaleEvent.class})
class LocaleEventTest {

    private static final String MESSAGE_LOCALE = "If you answered incorrectly, your language will be selected by default. Pls write me";
    private static final String RU_LOCALE_KEY = "1";
    private static final String RU_LOCALE = "ru-RU";
    private static final String EN_LOCALE_KEY = "2";
    private static final String EN_LOCALE = "en-EN";
    private static final String ES_LOCALE = "es-ES";

    @MockBean
    private L10nMessageService l10nMessageService;

    @MockBean
    private LocalizationConfig localizationConfig;

    @Autowired
    private Event localeEvent;

    private Locale beforeLocale;

    @BeforeEach
    public void setUp(){
        beforeLocale = Locale.getDefault();
    }

    @AfterEach
    public void tearDown(){
        Locale.setDefault(beforeLocale);
    }

    @Test
    void viewDescription() {

        var localeMap = Map.of(RU_LOCALE_KEY, RU_LOCALE, EN_LOCALE_KEY, EN_LOCALE);
        when(localizationConfig.getLocalMapping())
                .thenReturn(localeMap);

        when(l10nMessageService.getMessage(MESSAGE_GET_LOCALE))
                .thenReturn(MESSAGE_LOCALE);
        when(l10nMessageService.getMessage(MESSAGE_GET_LOCALE_BTW, RU_LOCALE_KEY, localeMap.get(RU_LOCALE_KEY)))
                .thenReturn(RU_LOCALE_KEY + " " + RU_LOCALE);
        when(l10nMessageService.getMessage(MESSAGE_GET_LOCALE_BTW, EN_LOCALE_KEY, localeMap.get(EN_LOCALE_KEY)))
                .thenReturn(RU_LOCALE_KEY + " " + EN_LOCALE);

        assertThat(localeEvent.viewDescription())
                .contains(MESSAGE_LOCALE)
                .contains(localeMap.get(RU_LOCALE_KEY))
                .contains(localeMap.get(EN_LOCALE_KEY));

        verify(localizationConfig, times(1)).getLocalMapping();
        verify(l10nMessageService, times(1)).getMessage(anyString());
        verify(l10nMessageService, times(2)).getMessage(anyString(), anyString(), anyString());
    }

    @Test
    void action() {
        var localeMap = Map.of(RU_LOCALE_KEY, RU_LOCALE, EN_LOCALE_KEY, EN_LOCALE);
        when(localizationConfig.getLocalMapping())
                .thenReturn(localeMap);
        when(l10nMessageService.getMessage(anyString())).thenReturn(MESSAGE_SUCCESS);

        assertThat(localeEvent.action(RU_LOCALE_KEY))
                .contains(MESSAGE_SUCCESS);

        assertThat(Locale.getDefault())
                .isEqualTo(Locale.forLanguageTag(RU_LOCALE));

        verify(localizationConfig, times(2)).getLocalMapping();
        verify(l10nMessageService, times(1)).getMessage(anyString());
    }


    @Test
    void action_keyNotFound() {
        var localeMap = Map.of(RU_LOCALE_KEY, RU_LOCALE, EN_LOCALE_KEY, EN_LOCALE);
        when(localizationConfig.getLocalMapping())
                .thenReturn(localeMap);
        when(l10nMessageService.getMessage(anyString(), anyString())).thenReturn(MESSAGE_NOT_CORRECT_PARAMETER);

        assertThat(localeEvent.action(ES_LOCALE))
                .contains(MESSAGE_NOT_CORRECT_PARAMETER);

        assertThat(Locale.getDefault())
                .isEqualTo(beforeLocale);

        verify(localizationConfig, times(1)).getLocalMapping();
        verify(l10nMessageService, times(1)).getMessage(anyString(), anyString());
    }

    @Test
    void action_argsNotCorrect() {

        when(l10nMessageService.getMessage(anyString(), anyString())).thenReturn(MESSAGE_INVALID_PARAMETERS);

        assertThat(localeEvent.action())
                .contains(MESSAGE_INVALID_PARAMETERS);

        assertThat(localeEvent.action(anyString(), anyString()))
                .contains(MESSAGE_INVALID_PARAMETERS);

        verify(l10nMessageService, times(2)).getMessage(anyString(), anyString());
    }

}