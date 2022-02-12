package ru.otus.spring.library.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.library.domain.Book;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {
    @Override
    @EntityGraph(value = "book-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Book> findAll();
}
