package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.repo.BookRepo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
@Import(BookService.class)
class BookServiceTest {

    private static final Long BOOK_CORRECT_ID = 2L;
    private static final String EXPECTED_BOOK_NAME = "Властелин колец: братство кольца";
    private static final String EXPECTED_GENRE_NAME = "роман-эпопея";
    private static final Long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_AUTHOR_NAME = "Толкин, Джон Рональд Руэл";
    private static final Long EXPECTED_AUTHOR_ID = 1L;

    @MockBean
    private CustomAclService customAclService;

    @MockBean
    private BookRepo bookRepository;

    @Autowired
    private BookService bookService;

    @Test
    void getById() {
        Book book = createExpectedBook();
        when(bookRepository.findById(anyLong()))
                .thenReturn(Optional.of(book));

        assertThat(bookService.getById(BOOK_CORRECT_ID))
                .isEqualTo(book);
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAll() {
        when(bookRepository.findAll())
                .thenReturn(List.of(new Book(), new Book()));

        assertThat(bookService.getAll())
                .hasSize(2);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void save() {
        Book bookBefore = Book.builder()
                .name("new book")
                .build();
        Book bookAfter = new Book(1L, bookBefore.getName());

        when(bookRepository.save(any()))
                .thenReturn(bookAfter);
        doNothing().when(customAclService).createAcl(any(), any(), any());
        assertThat(bookService.save(bookBefore))
                .isEqualTo(bookAfter);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void updateById() {
        Book bookAfter = Book.builder()
                .name("new book")
                .id(1L)
                .build();
        when(bookRepository.save(bookAfter))
                .thenReturn(bookAfter);

        when(bookRepository.findById(bookAfter.getId())).thenReturn(Optional.of(bookAfter));

        assertThat(bookService.updateById(bookAfter.getId(), bookAfter))
                .isEqualTo(bookAfter);
        verify(bookRepository, times(1)).findById(any());
    }

    @Test
    void deleteById() {
        doNothing().when(bookRepository).delete(any());
        when(bookRepository.findById(BOOK_CORRECT_ID)).thenReturn(Optional.of(mock(Book.class)));
        bookService.deleteById(BOOK_CORRECT_ID);
        verify(bookRepository, times(1)).delete(any());
    }


    private Book createExpectedBook() {
        return Book.builder()
                .id(BOOK_CORRECT_ID)
                .name(EXPECTED_BOOK_NAME)
                .genre(new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME))
                .authors(Set.of(new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME)))
                .build();
    }

}