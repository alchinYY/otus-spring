package ru.otus.spring.library.job.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import ru.otus.spring.library.domain.mongo.AuthorMng;
import ru.otus.spring.library.domain.mongo.GenreMng;
import ru.otus.spring.library.domain.sql.Author;
import ru.otus.spring.library.domain.sql.Genre;
import ru.otus.spring.library.job.listener.SqlToMngChunkListener;
import ru.otus.spring.library.job.listener.SqlToMngItemProcessListener;
import ru.otus.spring.library.job.listener.SqlToMngItemReadListener;
import ru.otus.spring.library.job.listener.SqlToMngItemWriteListener;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GenreStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private static final int CHUNK_SIZE = 5;
    private static final String OBJECT_ENTITY = "genre";
    private static final String STEP_NAME = "genre-to-genreMng";

    @Bean
    public Step transferGenresStep(ItemReader<Genre> reader, ItemWriter<GenreMng> writer,
                                   ItemProcessor<Genre, GenreMng> processor) {
        return stepBuilderFactory.get(STEP_NAME)
                .<Genre, GenreMng>chunk(CHUNK_SIZE)
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

