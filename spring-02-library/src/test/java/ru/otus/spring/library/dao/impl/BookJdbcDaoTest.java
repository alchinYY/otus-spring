package ru.otus.spring.library.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import ru.otus.spring.library.aop.AopDaoService;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.dao.impl.ext.BookListResultSetExtractor;
import ru.otus.spring.library.dao.impl.ext.BookResultSetExtractor;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.exception.RelationDataException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dao для работы с книгами должно")
@JdbcTest
@Import({BookJdbcDao.class, BookResultSetExtractor.class, BookListResultSetExtractor.class, AopDaoService.class})
class BookJdbcDaoTest {

    private static final Long BOOK_CORRECT_ID = 2L;
    private static final Long BOOK_NOT_CORRECT_ID = 100L;
    private static final Long BOOK_AFTER_SAVE_ID = 5L;
    private static final Long GENRE_NOT_CORRECT_ID = 100L;

    private static final String EXPECTED_BOOK_NAME = "Властелин колец: братство кольца";
    private static final String EXPECTED_BOOK_NAME_UPD = "Властелин колец: возвращение короля";
    private static final String EXPECTED_GENRE_NAME = "роман-эпопея";
    private static final Long EXPECTED_GENRE_ID = 1L;

    private static final String EXPECTED_AUTHOR_NAME = "Толкин, Джон Рональд Руэл";
    private static final Long EXPECTED_AUTHOR_ID = 1L;


    private static final String EXPECTED_GENRE_NAME_UPD = "сказка";
    private static final Long EXPECTED_GENRE_ID_UPD = 2L;
    private static final String EXPECTED_AUTHOR_NAME_UPD = "Пушкин Александр Сергеевич";
    private static final Long EXPECTED_AUTHOR_ID_UPD = 2L;
    private static final String EXPECTED_AUTHOR_NAME_UPD_2 = "Стругацкий Аркадий";
    private static final Long EXPECTED_AUTHOR_ID_UPD_2 = 3L;


    @Autowired
    private Dao<Long, Book> bookJdbcDao;

    @Autowired
    private AopDaoService aopDaoService;

    @Autowired
    private BookResultSetExtractor bookResultSetExtractor;

    @Autowired
    private BookListResultSetExtractor bookListResultSetExtractor;

    @BeforeTransaction
    void beforeTransaction() {
        System.out.println("-----------");
    }

    @AfterTransaction
    void afterTransaction() {
        System.out.println("-----------");
    }

    @Test
    @DisplayName("возвращать ожидаемую книгу по id")
    void getById() {
        Book book = bookJdbcDao.getById(BOOK_CORRECT_ID);

        assertThat(book)
                .usingRecursiveComparison().isEqualTo(createExpectedBook());
    }

    @Test
    @DisplayName("возвращать ошибку, если книги с заданным id нет")
    void getById_bookNotFound() {
        assertThatExceptionOfType(DomainNotFound.class)
                .isThrownBy(() -> bookJdbcDao.getById(BOOK_NOT_CORRECT_ID))
                .withMessage("Domain \"book\" not found");
    }

    @Test
    @DisplayName("корректно обновлять все поля в книге")
    void updateById() {
        Book bookExpected =  Book.builder()
                .id(BOOK_CORRECT_ID)
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD, EXPECTED_GENRE_NAME_UPD))
                .authors(List.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD, EXPECTED_AUTHOR_NAME_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2, EXPECTED_AUTHOR_NAME_UPD_2)
                ))
                .build();
        Book bookForUpdate = Book.builder()
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD))
                .authors(List.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2)
                ))
                .build();

        bookJdbcDao.updateById(BOOK_CORRECT_ID, bookForUpdate);
        Book bookActual = bookJdbcDao.getById(BOOK_CORRECT_ID);
        assertThat(bookActual)
                .usingRecursiveComparison().isEqualTo(bookExpected);
    }

    @Test
    @DisplayName("выдавать ошибку при поптыке обновить несуществующую книгу")
    void updateById_updateNonExistent() {
        Book bookForUpdate = Book.builder()
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD))
                .authors(List.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2)
                ))
                .build();

        assertThatExceptionOfType(DomainNotFound.class)
                .isThrownBy(() -> bookJdbcDao.updateById(BOOK_NOT_CORRECT_ID, bookForUpdate));
    }

    @Test
    @DisplayName("выдавать ошибку при некорректном заполнении поля жанр id")
    void updateById_withNotCorrectGenre() {
        Book bookForUpdate = Book.builder()
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(GENRE_NOT_CORRECT_ID))
                .authors(List.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2)
                ))
                .build();

        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> bookJdbcDao.updateById(BOOK_CORRECT_ID, bookForUpdate));
    }

    @Test
    @DisplayName("сохранять книгу")
    void save() {
        Book bookExpected = Book.builder()
                .id(BOOK_AFTER_SAVE_ID)
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD, EXPECTED_GENRE_NAME_UPD))
                .authors(List.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD, EXPECTED_AUTHOR_NAME_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2, EXPECTED_AUTHOR_NAME_UPD_2)
                ))
                .build();

        Book bookForSave = Book.builder()
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD))
                .authors(List.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2)
                ))
                .build();
        bookJdbcDao.save(bookForSave);
        Book bookActual = bookJdbcDao.getById(BOOK_AFTER_SAVE_ID);
        assertThat(bookActual)
                .usingRecursiveComparison().isEqualTo(bookExpected);
    }

    @Test
    @DisplayName("возвращать ожидаемый список книг")
    void getAll() {
        assertThat(bookJdbcDao.getAll())
                .hasSize(4)
                .contains(createExpectedBook());
    }

    @Test
    @DisplayName("удалять заданною книгу по ее id")
    void deleteById() {
        bookJdbcDao.deleteById(BOOK_CORRECT_ID);

        assertThat(bookJdbcDao.getAll())
                .hasSize(3)
                .doesNotContain(createExpectedBook());

        assertThatExceptionOfType(DomainNotFound.class)
                .isThrownBy(() -> bookJdbcDao.getById(BOOK_CORRECT_ID));
    }


    private Book createExpectedBook(){
        return Book.builder()
                .id(BOOK_CORRECT_ID)
                .name(EXPECTED_BOOK_NAME)
                .genre(new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME))
                .authors(List.of(new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME)))
                .build();
    }

}