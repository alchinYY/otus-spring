package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.dao.impl.ext.BookListResultSetExtractor;
import ru.otus.spring.library.dao.impl.ext.BookResultSetExtractor;
import ru.otus.spring.library.domain.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@DaoRepository
@SuppressWarnings({"SqlNoDataSourceInspection", "ConstantConditions", "SqlDialectInspection", "unchecked"})
public class BookJdbcDao implements Dao<Long, Book> {

    private final NamedParameterJdbcOperations jdbc;
    private final BookResultSetExtractor bookResultSetExtractor;
    private final BookListResultSetExtractor bookListResultSetExtractor;

    @Override
    public Book getById(Long id) {
        return jdbc.query(
                "SELECT " +
                        "b.id AS book_id, " +
                        "b.name AS book_name, " +
                        "a.id AS author_id, " +
                        "a.name AS author_name, " +
                        "g.id AS genre_id, " +
                        "g.name AS genre_name " +
                    "FROM books b " +
                        "LEFT JOIN authors_books ab on b.id = ab.book_id " +
                        "LEFT JOIN authors a on a.id = ab.author_id " +
                        "JOIN genres g ON b.genre_id=g.id WHERE b.id = :id", Map.of("id", id), bookResultSetExtractor
        );
    }

    @Override
    public void updateById(Long id, Book entity) {
        jdbc.update("UPDATE books b SET NAME = :name, genre_id = :genre_id " +
                        "WHERE id = :id;",
                Map.of("id", id, "name", entity.getName(), "genre_id", entity.getGenre().getId())
        );
        entity.setId(id);
        jdbc.update("DELETE FROM authors_books WHERE book_id = :id", Map.of("id", entity.getId()));
        insertBachAuthorBook(entity);
    }

    @Override
    public Book save(final Book entity) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("genre_id", entity.getGenre().getId());

        jdbc.update("INSER INTO books (name, genre_id) VALUES (:name, :genre_id);", mapSqlParameterSource, keyHolder);

        entity.setId(keyHolder.getKey().longValue());

        insertBachAuthorBook(entity);
        return entity;
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query(
                "SELECT " +
                        "b.id AS book_id, " +
                        "b.name AS book_name, " +
                        "a.id AS author_id, " +
                        "a.name AS author_name, " +
                        "g.id AS genre_id, " +
                        "g.name AS genre_name " +
                     "FROM books b " +
                        "LEFT JOIN authors_books ab ON b.id = ab.book_id " +
                        "LEFT JOIN authors a ON a.id = ab.author_id " +
                        "JOIN genres g ON b.genre_id=g.id", bookListResultSetExtractor
        );
    }

    @Override
    public void deleteById(Long id) {
        jdbc.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }


    private void insertBachAuthorBook(Book entity) {
        jdbc.batchUpdate("INSERT INTO authors_books (book_id, author_id) " +
                        "SELECT b.id, a.id " +
                        "FROM books b, authors a " +
                        "WHERE b.id = :book_id AND a.id = :author_id;",
                createBachParams(entity)
        );
    }

    private SqlParameterSource[] createBachParams(Book entity) {
        Map<String, Long>[] maps = new HashMap[entity.getAuthors().size()];
        for (int i = 0; i < entity.getAuthors().size(); i++) {
            maps[i] = new HashMap<>();
            maps[i].put("book_id", entity.getId());
            maps[i].put("author_id", entity.getAuthors().get(i).getId());
        }

        return SqlParameterSourceUtils
                .createBatch(maps);
    }

}
