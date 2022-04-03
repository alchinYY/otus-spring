package ru.otus.spring.library.job.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;

@RequiredArgsConstructor
@Slf4j
public class SqlToMngItemReadListener<S> implements ItemReadListener<S> {

    private final String entityName;

    @Override
    public void beforeRead() {
        log.info("Начало чтения:{}", entityName);
    }

    @Override
    public void afterRead(S s) {
        log.info("Конец чтения:{}", s);
    }

    @Override
    public void onReadError(Exception e) {
        log.info("Ошибка чтения::{}", e.getMessage());
    }
}
