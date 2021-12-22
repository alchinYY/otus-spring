package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(BookService.class)
class BookServiceTest {

    private static final Long BOOK_CORRECT_ID = 2L;
    private static final String EXPECTED_BOOK_NAME = "Властелин колец: братство кольца";
    private static final String EXPECTED_GENRE_NAME = "роман-эпопея";
    private static final Long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_AUTHOR_NAME = "Толкин, Джон Рональд Руэл";
    private static final Long EXPECTED_AUTHOR_ID = 1L;


    @MockBean
    private Dao<Long, Book> bookDao;

    @Autowired
    private BookService bookService;

    @Test
    void getById() {
        Book book = createExpectedBook();
        when(bookDao.getById(anyLong()))
                .thenReturn(book);

        assertThat(bookService.getById(BOOK_CORRECT_ID))
                .isEqualTo(book);
        verify(bookDao, times(1)).getById(anyLong());
    }

    @Test
    void getAll() {
        when(bookDao.getAll())
                .thenReturn(List.of(new Book(), new Book()));

        assertThat(bookService.getAll())
                .hasSize(2);
        verify(bookDao, times(1)).getAll();
    }

    @Test
    void save() {
        Book bookBefore = Book.builder()
                .name("new book")
                .build();
        Book bookAfter = new Book(1L, bookBefore.getName());

        when(bookDao.save(any()))
                .thenReturn(bookAfter);


        assertThat(bookService.save(bookBefore))
                .isEqualTo(bookAfter);
        verify(bookDao, times(1)).save(any());
    }

    @Test
    void updateById() {
        Book bookBefore = Book.builder()
                .name("old book")
                .id(1)
                .build();
        Book bookAfter = Book.builder()
                .name("new book")
                .id(1)
                .build();
        doNothing().when(bookDao)
                .updateById(bookBefore.getId(), bookAfter);
        when(bookDao.getById(bookAfter.getId())).thenReturn(bookAfter);

        assertThat(bookService.updateById(bookBefore.getId(), bookAfter))
                .isEqualTo(bookAfter);
        verify(bookDao, times(1)).updateById(any(), any());
        verify(bookDao, times(1)).getById(any());
    }

    @Test
    void deleteById() {
        doNothing().when(bookDao).deleteById(anyLong());
        bookService.deleteById(BOOK_CORRECT_ID);
        verify(bookDao, times(1)).deleteById(any());
    }


    private Book createExpectedBook() {
        return Book.builder()
                .id(BOOK_CORRECT_ID)
                .name(EXPECTED_BOOK_NAME)
                .genre(new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME))
                .authors(List.of(new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME)))
                .build();
    }

}