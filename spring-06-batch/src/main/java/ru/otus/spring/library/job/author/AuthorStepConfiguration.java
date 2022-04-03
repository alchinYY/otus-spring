package ru.otus.spring.library.job.author;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.library.domain.mongo.AuthorMng;
import ru.otus.spring.library.domain.sql.Author;
import ru.otus.spring.library.job.listener.SqlToMngChunkListener;
import ru.otus.spring.library.job.listener.SqlToMngItemProcessListener;
import ru.otus.spring.library.job.listener.SqlToMngItemReadListener;
import ru.otus.spring.library.job.listener.SqlToMngItemWriteListener;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private static final int CHUNK_SIZE = 2;
    private static final String OBJECT_ENTITY = "author";
    private static final String STEP_NAME = "author-to-authorMng";

    @Bean
    public Step transferAuthorsStep(ItemReader<Author> reader, ItemWriter<AuthorMng> writer,
                                    ItemProcessor<Author, AuthorMng> processor) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Author, AuthorMng>chunk(CHUNK_SIZE)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new SqlToMngItemReadListener<>(OBJECT_ENTITY))
                .listener(new SqlToMngItemWriteListener<>(OBJECT_ENTITY))
                .listener(new SqlToMngItemProcessListener<>(OBJECT_ENTITY))
                .listener(new SqlToMngChunkListener(OBJECT_ENTITY))
                .build();
    }
}

