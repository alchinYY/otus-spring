package ru.otus.spring.library.dao.impl.ext;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BookListResultSetExtractor implements ResultSetExtractor<List<Book>> {
    @Override
    public List<Book> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Book> books = new HashMap<>();
        while (rs.next()) {
            long id = rs.getLong("id");
            Book book = books.get(id);

            if (book == null) {
                book = createBook(rs);
                books.put(book.getId(), book);
            }
            addAuthors(rs, book.getAuthors());
        }

        return new ArrayList<>(books.values());
    }

    private Book createBook(ResultSet rs) throws SQLException {
        return Book.builder()
                .name(rs.getString("book_name"))
                .id(rs.getLong("book_id"))
                .genre(createGenre(rs))
                .authors(new ArrayList<>())
                .build();
    }

    private Genre createGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getLong("genre_id"),
                rs.getString("genre_name")
        );
    }

    private void addAuthors(ResultSet rs, List<Author> authors) throws SQLException {
        authors.add(
                new Author(
                        rs.getLong("author_id"),
                        rs.getString("author_name"))
        );
    }
}
