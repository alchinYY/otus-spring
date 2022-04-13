package ru.otus.spring.books.healthindicators;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "logfile.name=src/test/resources/log.log")
@Import(LogFileSizeIndicator.class)
class LogFileSizeIndicatorTest {

    private static final long TEST_LOG_FILE_SIZE = 5;

    @Autowired
    private LogFileSizeIndicator logFileSizeIndicator;

    @Test
    void health() {

        Health health = logFileSizeIndicator.health();
        assertThat(health.getDetails().get("size"))
                .isEqualTo(TEST_LOG_FILE_SIZE);

    }
}