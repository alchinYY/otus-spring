package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;

import java.util.List;
import java.util.Map;

@DaoRepository
@RequiredArgsConstructor
@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
public class GenreJdbcDao implements Dao<Long, Genre> {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Genre> genreMapper;

    @Override
    public Genre getById(Long id) {
        return jdbc.queryForObject(
                "SELECT id, name FROM genres WHERE id = :id", Map.of("id", id), genreMapper
        );
    }

    @Override
    public void updateById(Long id, Genre entity) {
        int rowCount = jdbc.update("UPDATE genres SET name = :name WHERE id = :id;",
                Map.of("id", id, "name", entity.getName())
        );
        if(rowCount == 0){
            throw new DomainNotFound(id);
        }
    }

    @Override
    public Long save(Genre entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", entity.getName());
        jdbc.update("INSERT INTO genres (name) VALUES :name;", mapSqlParameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query(
                "SELECT id, name FROM genres", genreMapper
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("DELETE from genres WHERE id = :id", Map.of("id", id));
    }
}
