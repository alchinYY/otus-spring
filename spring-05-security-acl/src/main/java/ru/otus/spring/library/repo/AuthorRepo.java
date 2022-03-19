package ru.otus.spring.library.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;
import ru.otus.spring.library.domain.Author;

import java.util.List;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Long> {
    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Author> findAll();
}
