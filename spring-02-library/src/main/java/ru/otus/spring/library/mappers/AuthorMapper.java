package ru.otus.spring.library.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Author;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Author.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }


}
