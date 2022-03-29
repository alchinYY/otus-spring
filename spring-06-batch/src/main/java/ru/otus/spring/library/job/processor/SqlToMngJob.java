package ru.otus.spring.library.job.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.job.MigrationJob;


@Component
@RequiredArgsConstructor
public class SqlToMngJob implements MigrationJob {

    private final JobLauncher jobLauncher;
    private final Job importUserJob;

    @Override
    public void migrate() {
        try {
            JobExecution execution = jobLauncher.run(importUserJob, new JobParametersBuilder()
                    .toJobParameters());
            System.out.println(execution);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
