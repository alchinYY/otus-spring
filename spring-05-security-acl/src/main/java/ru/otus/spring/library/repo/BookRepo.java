package ru.otus.spring.library.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import ru.otus.spring.library.domain.Book;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(value = "book-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    @PostFilter("hasPermission(filterObject, 'READ') or hasAnyRole('EDITOR')")
    List<Book> findAll();

    @Override
    @PreAuthorize("hasAnyRole('BOOK_CREATOR')")
    <S extends Book> S save(S entity);

    @Override
    @PreAuthorize("hasPermission(#entity, 'WRITE')")
    void delete(Book entity);

    @Override
    @PreAuthorize("hasPermission(#id, 'ru.otus.spring.library.domain.Book', 'WRITE')")
    void deleteById(Long id);
}
