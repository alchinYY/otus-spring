package ru.otus.spring.library.job.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@RequiredArgsConstructor
@Slf4j
public class SqlToMngItemProcessListener<S, M> implements ItemProcessListener<S, M> {

    private final String entityName;

    @Override
    public void beforeProcess(S o) {
        log.info("Начало обработки {} :{}",entityName, o);
    }

    @Override
    public void afterProcess(S o1, M o2)  {
        log.info("Конец обработки.Получилось::{}", o2);
    }

    @Override
    public void onProcessError(S o, Exception e) {
        log.warn("Ошибка обработки::{}\n{}", o, e.getMessage());
    }
}
