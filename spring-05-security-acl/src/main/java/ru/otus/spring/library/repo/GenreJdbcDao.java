package ru.otus.spring.library.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;
import ru.otus.spring.library.domain.Genre;

import java.util.List;

@Repository
public interface GenreJdbcDao extends JpaRepository<Genre, Long> {

    @Override
    @PostFilter("hasPermission(filterObject, 'READ') or hasAnyRole('EDITOR')")
    List<Genre> findAll();

}
