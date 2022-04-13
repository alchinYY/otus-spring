package ru.otus.spring.books.healthindicators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.File;


@Component
@Slf4j
public class LogFileSizeIndicator implements HealthIndicator {

    private final File logFile;

    public LogFileSizeIndicator(@Value("${logfile.name}") String logFileName) {
        logFile = new File(logFileName);
    }

    @Override
    public Health health() {
        if(logFile.exists()) {
            var fileSize = logFile.length();
            log.info("log-file size = {} b", fileSize);
            return Health.up()
                    .withDetail("size", fileSize)
                    .build();
        } else {
            log.info("log-file not found");
            return Health.down()
                    .withDetail("message", "log file not exist!")
                    .build();
        }
    }
}
