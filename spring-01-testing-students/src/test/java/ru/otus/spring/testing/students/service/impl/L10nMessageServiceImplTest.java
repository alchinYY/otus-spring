package ru.otus.spring.testing.students.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ru.otus.spring.testing.students.Application;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class L10nMessageServiceImplTest {

    private final static String MESSAGE_TEST = "test.message";
    private final static String MESSAGE_EXTENDS = "test";

    private final static String MESSAGE_TEST_ARGS = "test.message.args";
    private final static String MESSAGE_ARGS = "args";
    private final static String MESSAGE_EXTENDS_ARGS = "test args";

    private final static String LOCALE_PATH = "i18n/messages";

    private L10nMessageServiceImpl l10nMessageService;

    @BeforeEach
    public void setup(){
        new ResourceBundleMessageSource();
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames(LOCALE_PATH);
        l10nMessageService = new L10nMessageServiceImpl(messageSource);
    }

    @Test
    void getMessage() {
        assertThat(l10nMessageService.getMessage(MESSAGE_TEST))
                .isEqualTo(MESSAGE_EXTENDS);

        assertThat(l10nMessageService.getMessage(MESSAGE_TEST_ARGS, MESSAGE_ARGS))
                .isEqualTo(MESSAGE_EXTENDS_ARGS);

    }
}