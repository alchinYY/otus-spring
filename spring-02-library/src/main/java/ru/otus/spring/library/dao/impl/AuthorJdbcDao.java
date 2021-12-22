package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.exception.DomainNotFound;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@DaoRepository
@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection"})
public class AuthorJdbcDao implements Dao<Long, Author> {

    private final NamedParameterJdbcOperations jdbc;
    private final RowMapper<Author> authorMapper;

    @Override
    public Author getById(Long id) {
        return jdbc.queryForObject(
                "SELECT id, name FROM authors WHERE id = :id", Map.of("id", id), authorMapper
        );
    }

    @Override
    public void updateById(Long id, Author entity) {
        int rowCount = jdbc.update("UPDATE authors SET name = :name WHERE id = :id;",
                Map.of("id", id, "name", entity.getName())
        );
        if(rowCount == 0){
            throw new DomainNotFound(id);
        }
    }

    @Override
    public Long save(Author entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", entity.getName());
        jdbc.update("insert into authors (name) values :name;", mapSqlParameterSource, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query(
                "select id, name from authors", authorMapper
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("delete from authors where id = :id", Map.of("id", id));
    }
}
