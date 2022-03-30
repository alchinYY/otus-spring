package ru.otus.spring.library.job.author;

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
import ru.otus.spring.library.domain.mongo.AuthorMng;
import ru.otus.spring.library.domain.sql.Author;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuthorMigrationJobConfiguration {

    @StepScope
    @Bean
    public ItemReader<Author> sqlAuthorReader(JpaRepository<Author, Long> authorRepo) {
        return new RepositoryItemReaderBuilder<Author>()
                .name("authorRepoItemReader")
                .repository(authorRepo)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<AuthorMng> mongoAuthorWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<AuthorMng>()
                .collection("authors")
                .template(mongoOperations)
                .delete(false)
                .build();
    }

    @StepScope
    @Bean
    public AuthorMigrationItemProcessor processorAuthor() {
        return new AuthorMigrationItemProcessor();
    }
}
