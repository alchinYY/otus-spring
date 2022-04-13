package ru.otus.spring.library.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CommonJobConfig {

    public static final String SQL_TO_MONGO_JOB = "SqlToMngJob";

    private final StepBuilderFactory stepBuilderFactory;
    private final CleanUpService cleanUpService;
    private final JobBuilderFactory jobBuilderFactory;

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Step cleanUpStep() {
        return this.stepBuilderFactory.get("cleanUpStep")
                .tasklet(cleanUpTasklet())
                .build();
    }

    @Bean
    public JobExecutionListener jobExecutionListener(){
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("Job sqlTransferToMongo is started");
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                log.info("Job sqlTransferToMongo is finished");
            }
        };
    }

    @Bean
    public Job sqlTransferToMongo(Step transferAuthorsStep, Step transferGenresStep, Step transferBooksStep, Step cleanUpStep) {
        return jobBuilderFactory.get("SqlToMngJob")
                .incrementer(new RunIdIncrementer())
                .start(transferAuthorsStep)
                .next(transferGenresStep)
                .next(transferBooksStep)
                .next(cleanUpStep)
                .listener(jobExecutionListener())
                .build();
    }
}
