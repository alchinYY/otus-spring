package ru.otus.spring.testing.students.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import ru.otus.spring.testing.students.config.aop.ServiceWithAop;
import ru.otus.spring.testing.students.service.L10nMessageService;

import java.util.Locale;

@RequiredArgsConstructor
@ServiceWithAop
@Slf4j
public class L10nMessageServiceImpl implements L10nMessageService {

    private final MessageSource messageSource;

    @Override
    public String getMessage(String value, Object... args) {
        return messageSource.getMessage(value, args, Locale.getDefault());
    }
}
