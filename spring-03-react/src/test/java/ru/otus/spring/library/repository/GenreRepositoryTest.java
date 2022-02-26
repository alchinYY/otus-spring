package ru.otus.spring.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import ru.otus.spring.library.domain.Genre;

import java.util.Comparator;


@DataMongoTest
class GenreRepositoryTest {

    private static final String GENRE_1_ID = "id1";
    private static final String GENRE_2_ID = "id2";

    private static final String GENRE_1_NAME = "genre1";
    private static final String GENRE_2_NAME = "genre2";

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void findAll() {

        Genre genre1 = new Genre(GENRE_1_ID, GENRE_1_NAME);
        Genre genre2 = new Genre(GENRE_2_ID, GENRE_2_NAME);

        Flux<Genre> genreFlux = Flux.just(genre1, genre2)
                .flatMap(this.genreRepository::save)
                .thenMany(genreRepository.findAll())
                .sort(Comparator.comparing(Genre::getId));

        StepVerifier
                .create(genreFlux)
                .expectNextMatches(genre -> genre.equals(genre1))
                .expectNextMatches(genre -> genre.equals(genre2))
                .verifyComplete();


    }
}