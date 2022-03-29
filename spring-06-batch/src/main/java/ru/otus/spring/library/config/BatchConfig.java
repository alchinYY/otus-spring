package ru.otus.spring.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import ru.otus.spring.library.domain.sql.Book;

@EnableBatchProcessing
@Configuration
@Slf4j
public class BatchConfig {

    @Bean
    public JobRegistryBeanPostProcessor postProcessor(JobRegistry jobRegistry) {
        var processor = new JobRegistryBeanPostProcessor();
        processor.setJobRegistry(jobRegistry);
        return processor;
    }

    @Bean
    public ItemReadListener<Book> bookItemReadListener() {
        return new ItemReadListener<>() {
            public void beforeRead() {
                log.info("Начало чтения");
            }

            public void afterRead(@NonNull Book b) {
                log.info("Конец чтения");
            }

            public void onReadError(@NonNull Exception e) {
                log.info("Ошибка чтения");
            }
        };
    }

}
