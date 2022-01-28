package ru.otus.spring.library.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.spring.library.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findByGenreId(Integer genreId);

    List<Book> findByAuthorsId(Integer authorId);

    Optional<Book> findByCommentsId(Integer commentId);
}
