package ru.otus.spring.books.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.spring.books.domain.Book;

@RepositoryRestResource(collectionResourceRel = "books", path = "books")
public interface BookRestRepo extends JpaRepository<Book, Long> {

    Book findByName(@Param("name") String name);

}
