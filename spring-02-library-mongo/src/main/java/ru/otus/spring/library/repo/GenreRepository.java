package ru.otus.spring.library.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.spring.library.domain.Genre;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
}
