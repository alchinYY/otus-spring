package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.repo.BookRepository;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(BookService.class)
class BookServiceTest {

    private static final int BOOK_CORRECT_ID = 2;
    private static final String EXPECTED_BOOK_NAME = "Властелин колец: братство кольца";
    private static final String EXPECTED_GENRE_NAME = "роман-эпопея";
    private static final int EXPECTED_GENRE_ID = 1;

    private static final String EXPECTED_AUTHOR_NAME = "Толкин, Джон Рональд Руэл";
    private static final int EXPECTED_AUTHOR_ID = 1;

    @Configuration
    public static class BookServiceTestConf{}

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

    @MockBean
    private SequenceGeneratorService<Integer> sequenceGeneratorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private AuthorService authorService;

    @Test
    void getById() {
        Book book = createExpectedBook();
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        assertThat(bookService.getById(BOOK_CORRECT_ID)).isEqualTo(book);
        verify(bookRepository, times(1)).findById(anyInt());
    }

    @Test
    void getAll() {
        when(bookRepository.findAll()).thenReturn(List.of(new Book(), new Book()));

        assertThat(bookService.getAll()).hasSize(2);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void save() {
        final int newBookId = 1;
        Book bookBefore = createBook(null, "new book");
        Book bookAfter = createBook(newBookId, bookBefore.getName());

        when(sequenceGeneratorService.generateSequence(anyString())).thenReturn(newBookId);
        when(bookRepository.save(any())).thenReturn(bookAfter);
        when(genreService.getById(anyInt())).thenReturn(bookBefore.getGenre());

        for (Author author : bookAfter.getAuthors()) {
            when(authorService.getById(author.getId())).thenReturn(author);
        }

        assertThat(bookService.save(bookBefore)).isEqualTo(bookAfter);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void updateById() {
        Book bookAfter = createBook(1, "new book");
        when(bookRepository.save(bookAfter)).thenReturn(bookAfter);
        when(bookRepository.findById(bookAfter.getId())).thenReturn(Optional.of(bookAfter));
        assertThat(bookService.updateById(bookAfter.getId(), bookAfter)).isEqualTo(bookAfter);
        verify(bookRepository, times(1)).findById(any());
    }

    @Test
    void deleteById() {
        doNothing().when(bookRepository).deleteById(anyInt());
        bookService.deleteById(BOOK_CORRECT_ID);
        verify(bookRepository, times(1)).deleteById(any());
    }


    private Book createExpectedBook() {
        return Book.builder()
                .id(BOOK_CORRECT_ID)
                .name(EXPECTED_BOOK_NAME)
                .genre(new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME))
                .authors(List.of(new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME)))
                .build();
    }

    private Book createBook(Integer id, String name){
        return Book.builder()
                .name(name)
                .genre(new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME))
                .authors(List.of(new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME)))
                .id(id)
                .build();
    }

}