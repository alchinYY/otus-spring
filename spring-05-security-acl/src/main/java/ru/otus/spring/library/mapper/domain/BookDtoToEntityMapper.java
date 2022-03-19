package ru.otus.spring.library.mapper.domain;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.GenreDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookDtoToEntityMapper extends PropertyMap<BookDto, Book> {


    @Override
    protected void configure() {
        Converter<GenreDto, Genre> converterGenre = new AbstractConverter<>() {
            @Override
            protected Genre convert(GenreDto source) {
                return new Genre(source.getId(), source.getName());
            }
        };
        Converter<List<AuthorDto>, Set<Author>> converterAuthor = new AbstractConverter<>() {
            @Override
            protected Set<Author> convert(List<AuthorDto> source) {
                return Optional.ofNullable(source).map(authors -> authors.stream().map(a -> new Author(a.getId(), a.getName())).collect(Collectors.toSet()))
                        .orElse(null);
            }
        };
        using(converterGenre).map(source.getGenre(), destination.getGenre());
        using(converterAuthor).map(source.getAuthors(), destination.getAuthors());
    }

}
