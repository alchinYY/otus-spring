package ru.otus.spring.library.dao.impl.ext;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class BookResultSetExtractor implements ResultSetExtractor<Book> {
    @Override
    public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Author> authors = new ArrayList<>();
        Book book = null;
        while (rs.next()) {
            book = createBook(rs, book);
            addAuthors(rs, authors);
        }
        return Optional.ofNullable(book)
                .map(b -> {
                    b.setAuthors(authors);
                    return b;
                })
                .orElseThrow(() -> new DomainNotFound("book"));
    }

    private Book createBook(ResultSet rs, Book book) throws SQLException {
        return Optional.ofNullable(book)
                .orElse(
                        Book.builder()
                        .id(rs.getLong("book_id"))
                        .name(rs.getString("book_name"))
                        .genre(createGenre(rs))
                        .build()
                );
    }

    private Genre createGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getLong("genre_id"),
                rs.getString("genre_name")
        );
    }

    private void addAuthors(ResultSet rs, List<Author> authors) throws SQLException {
        long id = rs.getLong("author_id");
        if (id != 0) {
            authors.add(
                    new Author(id, rs.getString("author_name"))
            );
        }
    }
}

