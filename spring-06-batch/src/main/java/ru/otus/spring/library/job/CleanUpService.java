package ru.otus.spring.library.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CleanUpService {
    public void cleanUp() throws Exception {
        log.info("Выполняю завершающие мероприятия...");
        Thread.sleep(1000);
        log.info("Завершающие мероприятия закончены");
    }
}