package ru.otus.spring.library.job.book;

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
import ru.otus.spring.library.domain.mongo.BookMng;
import ru.otus.spring.library.domain.sql.Book;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BookMigrationJobConfiguration {

    @StepScope
    @Bean
    public ItemReader<Book> sqlBookReader(JpaRepository<Book, Long> bookRepo) {
        return new RepositoryItemReaderBuilder<Book>()
                .name("bookRepoItemReader")
                .repository(bookRepo)
                .methodName("findAll")
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<BookMng> mongoBookWriter(MongoOperations mongoOperations) {
        return new MongoItemWriterBuilder<BookMng>()
                .collection("books")
                .template(mongoOperations)
                .delete(false)
                .build();
    }

    @StepScope
    @Bean
    public BookMigrationItemProcessor processorBook() {
        return new BookMigrationItemProcessor();
    }

}
