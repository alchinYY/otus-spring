package ru.otus.spring.library.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.spring.library.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {

    Flux<Genre> findAll();
}
