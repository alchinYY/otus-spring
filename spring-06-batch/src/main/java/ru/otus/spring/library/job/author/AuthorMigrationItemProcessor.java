package ru.otus.spring.library.job.author;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.spring.library.domain.mongo.AuthorMng;
import ru.otus.spring.library.domain.sql.Author;

public class AuthorMigrationItemProcessor implements ItemProcessor<Author, AuthorMng> {

    @Override
    public AuthorMng process(Author author) {
        return new AuthorMng(author.getId().toString(), author.getName());
    }
}