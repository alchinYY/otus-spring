package ru.otus.spring.library.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.library.domain.sql.Genre;

@Repository
public interface GenreRepo extends JpaRepository<Genre, Long> {
}
