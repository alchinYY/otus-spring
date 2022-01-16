package ru.otus.spring.library.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.spring.library.domain.Author;

public interface AuthorRepository extends CrudRepository<Author, Integer> {
}
