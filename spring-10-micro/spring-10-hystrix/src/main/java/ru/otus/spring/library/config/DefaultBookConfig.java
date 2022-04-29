package ru.otus.spring.library.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Configuration
@ConfigurationProperties(prefix = "book.default")
@Data
public class DefaultBookConfig {
    private String name;
    private String author;
    private String genre;
    private long id;

    private Book defaultBook;

    @PostConstruct
    public void init() {
        defaultBook = new Book(id, name);
        defaultBook.setAuthors(Collections.singleton(new Author(id, author)));
        defaultBook.setGenre(new Genre(id, genre));
    }
}
