package ru.otus.spring.library.dao.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import ru.otus.spring.library.aop.AopDaoService;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с книгами должно")
@DataJpaTest
@Import({BookJdbcDao.class, AopDaoService.class})
class BookJdbcDaoTest {

    private static final Long BOOK_CORRECT_ID = 2L;
    private static final Long BOOK_NOT_CORRECT_ID = 100L;
    private static final Long BOOK_AFTER_SAVE_ID = 5L;

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
    private TestEntityManager em;

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
        assertThat(bookJdbcDao.getById(BOOK_CORRECT_ID))
                .isPresent()
                .get()
                .isEqualTo(createExpectedBook());
    }

    @Test
    @DisplayName("возвращать isEmpty, если книги с заданным id нет")
    void getById_bookNotFound() {
        assertThat(bookJdbcDao.getById(BOOK_NOT_CORRECT_ID))
                .isEmpty();
    }

    @Test
    @DisplayName("корректно обновлять все поля в книге")
    void updateById() {
        Book bookExpected = Book.builder()
                .id(BOOK_CORRECT_ID)
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD, EXPECTED_GENRE_NAME_UPD))
                .authors(Set.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD, EXPECTED_AUTHOR_NAME_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2, EXPECTED_AUTHOR_NAME_UPD_2)
                ))
                .build();
        Book bookForUpdate = createBookAfterUpdate(EXPECTED_GENRE_ID_UPD, EXPECTED_GENRE_NAME_UPD);
        bookForUpdate.setId(BOOK_CORRECT_ID);

        bookJdbcDao.save(bookForUpdate);

        assertThat(em.find(Book.class, BOOK_CORRECT_ID))
                .isEqualTo(bookExpected);
    }


    @Test
    @DisplayName("сохранять книгу")
    void save() {
        Book bookExpected = Book.builder()
                .id(BOOK_AFTER_SAVE_ID)
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(EXPECTED_GENRE_ID_UPD, EXPECTED_GENRE_NAME_UPD))
                .authors(Set.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD, EXPECTED_AUTHOR_NAME_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2, EXPECTED_AUTHOR_NAME_UPD_2)
                ))
                .build();

        Book bookForSave = createBookAfterUpdate(EXPECTED_GENRE_ID_UPD, EXPECTED_GENRE_NAME_UPD);
        Book afterSave = bookJdbcDao.save(bookForSave);

        Book bookActual = em.find(Book.class, afterSave.getId());
        assertThat(bookActual)
                .isEqualTo(bookExpected);
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

        assertThat(bookJdbcDao.getById(BOOK_CORRECT_ID))
                .isEmpty();
    }

    private Book createBookAfterUpdate(Long expectedGenreIdUpd, String genreName) {
        return Book.builder()
                .name(EXPECTED_BOOK_NAME_UPD)
                .genre(new Genre(expectedGenreIdUpd, genreName))
                .authors(Set.of(
                        new Author(EXPECTED_AUTHOR_ID_UPD, EXPECTED_AUTHOR_NAME_UPD),
                        new Author(EXPECTED_AUTHOR_ID_UPD_2, EXPECTED_AUTHOR_NAME_UPD_2)
                ))
                .build();
    }

    private Book createExpectedBook() {
        return Book.builder()
                .id(BOOK_CORRECT_ID)
                .comments(List.of())
                .name(EXPECTED_BOOK_NAME)
                .genre(new Genre(EXPECTED_GENRE_ID, EXPECTED_GENRE_NAME))
                .authors(Set.of(new Author(EXPECTED_AUTHOR_ID, EXPECTED_AUTHOR_NAME)))
                .build();
    }

}