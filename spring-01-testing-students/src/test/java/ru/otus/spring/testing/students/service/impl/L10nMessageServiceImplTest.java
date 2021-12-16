package ru.otus.spring.testing.students.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import ru.otus.spring.testing.students.service.L10nMessageService;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class L10nMessageServiceImplTest {

    private final static String MESSAGE_TEST = "test.message";
    private final static String MESSAGE_EXTENDS = "test";
    private final static String MESSAGE_TEST_ARGS = "test.message.args";
    private final static String MESSAGE_ARGS = "args";
    private final static String MESSAGE_EXTENDS_ARGS = "test args";


    @Autowired
    private L10nMessageService l10nMessageService;

    @Autowired
    private MessageSource messageSource;

    @BeforeEach
    public void setup() {
        Locale.setDefault(Locale.forLanguageTag("en_EN"));
    }

    @Test
    void getMessage() {
        assertThat(l10nMessageService.getMessage(MESSAGE_TEST))
                .isEqualTo(MESSAGE_EXTENDS);
    }

    @Test
    void getMessage_withArgs() {
        assertThat(l10nMessageService.getMessage(MESSAGE_TEST_ARGS, MESSAGE_ARGS))
                .isEqualTo(MESSAGE_EXTENDS_ARGS);
    }
}