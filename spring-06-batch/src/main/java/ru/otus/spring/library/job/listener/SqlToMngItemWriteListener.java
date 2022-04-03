package ru.otus.spring.library.job.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SqlToMngItemWriteListener<M> implements ItemWriteListener<M> {

    private final String entityName;


    @Override
    public void beforeWrite(List<? extends M> list) {
        log.info("Начало записи:{}", entityName);
    }

    @Override
    public void afterWrite(List<? extends M> list) {
        log.info("Конец записи::{}", entityName);
    }

    @Override
    public void onWriteError(Exception e, List<? extends M> list) {
        log.info("Ошибка записи::{}", e.getMessage());
    }
}
