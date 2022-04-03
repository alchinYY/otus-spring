package ru.otus.spring.library.job.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.spring.library.domain.mongo.GenreMng;
import ru.otus.spring.library.domain.sql.Genre;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GenreMigrationJobConfiguration {

    @StepScope
    @Bean
    public ItemReader<Genre> sqlGenreReader(JpaRepository<Genre, Long> genreRepo) {
        return new RepositoryItemReaderBuilder<Genre>()
                .name("genreRepoItemReader")
                .repository(genreRepo)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<GenreMng> mongoGenreWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<GenreMng>()
                .collection("genres")
                .template(mongoOperations)
                .delete(false)
                .build();
    }

    @StepScope
    @Bean
    public GenreMigrationItemProcessor processorGenre() {
        return new GenreMigrationItemProcessor();
    }

}
