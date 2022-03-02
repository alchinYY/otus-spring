package ru.otus.spring.library.handler;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.repository.AuthorRepository;
import ru.otus.spring.library.repository.BookRepository;
import ru.otus.spring.library.repository.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = BookRepoHandler.class)
class BookRepoHandlerTest {

    @Autowired
    private BookRepoHandler bookRepoHandler;

    @MockBean
    private BookRepository bookRepository;
    @MockBean
    private AuthorRepository authorRepository;
    @MockBean
    private GenreRepository genreRepository;
    @MockBean
    private ModelMapper modelMapper;

    @Test
    void delete() {

        when(bookRepository.deleteById(anyString())).thenReturn(Mono.empty());

        assertThat(bookRepoHandler.delete("id"))
                .isEqualTo(Mono.empty());
    }

    @Test
    void save() {

        Book book = new Book("id", "name");
        Author author = new Author("a_id", "a_name");
        Genre genre = new Genre("g_id", "g_name");

        book.setAuthors(List.of(author.getId()));
        book.setGenre(genre.getId());


        BookDto bookDto = new BookDto();
        bookDto.setName(book.getName());
        bookDto.setAuthors(List.of(new AuthorDto(author.getId(), author.getName())));
        bookDto.setGenre(new GenreDto(genre.getId(), genre.getName()));

        when(bookRepository.save(any())).thenReturn(Mono.just(book));
        when(authorRepository.findAllById(book.getAuthors())).thenReturn(Flux.just(author));
        when(genreRepository.findById(book.getGenre())).thenReturn(Mono.just(genre));

        when(modelMapper.map(any(), eq(Book.class))).thenReturn(book);
        when(modelMapper.map(any(), eq(AuthorDto.class))).thenReturn(bookDto.getAuthors().get(0));
        when(modelMapper.map(any(), eq(GenreDto.class))).thenReturn(bookDto.getGenre());


        StepVerifier
                .create(bookRepoHandler.save(bookDto))
                .assertNext(b -> assertEquals(b.getId(), book.getId()))
                .verifyComplete();

    }

    @Test
    void toEntity() {
        Book book = new Book("b_id", "b_name");

        when(modelMapper.map(any(BookDto.class), eq(Book.class)))
                .thenReturn(book);
        AuthorDto authorDto = new AuthorDto("a_id", "a_name");
        GenreDto genreDto = new GenreDto("g_id", "g_name");

        BookDto bookDto = new BookDto(book.getId(), book.getName());
        bookDto.setAuthors(List.of(authorDto));
        bookDto.setGenre(genreDto);

        assertThat(bookRepoHandler.toEntity(bookDto))
                .extracting(Book::getId, Book::getName, Book::getAuthors, Book::getGenre)
                .contains(book.getId(), book.getName(), List.of(authorDto.getId()), genreDto.getId());
    }
}