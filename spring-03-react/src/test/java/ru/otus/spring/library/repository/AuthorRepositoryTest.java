package ru.otus.spring.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.spring.library.domain.Author;

import java.util.Comparator;

@DataMongoTest
class AuthorRepositoryTest {

    private static final String AUTHOR_1_ID = "id1";
    private static final String AUTHOR_2_ID = "id2";

    private static final String AUTHOR_1_NAME = "author1";
    private static final String AUTHOR_2_NAME = "author2";

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void findAll() {

        Author author1 = new Author(AUTHOR_1_ID, AUTHOR_1_NAME);
        Author author2 = new Author(AUTHOR_2_ID, AUTHOR_2_NAME);

        Flux<Author> authorFlux = Flux.just(author1, author2)
                .flatMap(this.authorRepository::save)
                .thenMany(authorRepository.findAll())
                .sort(Comparator.comparing(Author::getId));

        StepVerifier
                .create(authorFlux)
                .expectNextMatches(author -> author.equals(author1))
                .expectNextMatches(author -> author.equals(author2))
                .verifyComplete();

    }
}