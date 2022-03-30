package ru.otus.spring.library.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class SqlToMngJob implements MigrationJob {

    private final JobLauncher jobLauncher;
    private final Job sqlTransferToMongo;


    @Override
    public void migrate() {
        try {
            JobExecution executionA = jobLauncher.run(sqlTransferToMongo, new JobParametersBuilder()
            .addDate("start", new Date()).toJobParameters());
            log.info("result::{}", executionA);
        } catch (Exception ex) {
            log.warn("result::{}", ex.getMessage());
        }
    }
}
