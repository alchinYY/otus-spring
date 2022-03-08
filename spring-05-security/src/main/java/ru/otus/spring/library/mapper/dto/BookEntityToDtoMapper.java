package ru.otus.spring.library.mapper.dto;

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
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookEntityToDtoMapper extends PropertyMap<Book, BookDto> {

    @Override
    protected void configure() {
        Converter<Genre, GenreDto> converterGenre = new AbstractConverter<>() {
            @Override
            protected GenreDto convert(Genre source) {
                return new GenreDto(source.getId(), source.getName());
            }
        };
        Converter<Set<Author>, List<AuthorDto>> converterAuthor = new AbstractConverter<>() {
            @Override
            protected List<AuthorDto> convert(Set<Author> source) {
                return source.stream().map(a -> new AuthorDto(a.getId(), a.getName())).collect(Collectors.toList());
            }
        };
        using(converterGenre).map(source.getGenre(), destination.getGenre());
        using(converterAuthor).map(source.getAuthors(), destination.getAuthors());
    }

}
