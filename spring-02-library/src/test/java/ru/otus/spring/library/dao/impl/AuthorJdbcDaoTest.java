package ru.otus.spring.library.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.transaction.AfterTransaction;
import ru.otus.spring.library.aop.AopDaoService;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.mappers.AuthorMapper;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Dao для работы с авторами должно")
@JdbcTest
@Import({AuthorJdbcDao.class, AuthorMapper.class, AopDaoService.class})
class AuthorJdbcDaoTest {

    private static final int AUTHORS_BEFORE_SIZE = 5;
    private static final Long AUTHOR_NOT_CORRECT_FOR_REMOVE_ID = 1L;
    private static final Long AUTHOR_CORRECT_FOR_REMOVE_ID = 5L;
    private static final String AUTHOR_CORRECT_FOR_REMOVE_NAME = "Неизвестный автор";
    private static final String AUTHOR_CORRECT_NAME = "Толкин, Джон Рональд Руэл";
    private static final String AUTHOR_NEW_CORRECT_NAME = "Толкин-Джон Рональд Руэл";
    private static final Long AUTHOR_CORRECT_ID = 1L;

    private static final Long AUTHOR_NEW_ID = 6L;
    private static final String AUTHOR_NEW_NAME = "Книжек еще не написал";

    @Autowired
    private Dao<Long, Author> genreDao;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private AopDaoService aopDaoService;

    @BeforeEach
    void setUp() {
    }


    @AfterTransaction
    void afterTransaction() {
    }

    @Test
    @DisplayName("возвращать ожидаемого автора по id")
    void getById() {
        Author authorExpected = new Author(AUTHOR_CORRECT_ID, AUTHOR_CORRECT_NAME);

        assertThat(genreDao.getById(authorExpected.getId()))
                .isEqualTo(authorExpected);
    }

    @Test
    @DisplayName("возвращать ошибку, если автора по id нет")
    void getById_idNotFound() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> genreDao.getById(AUTHOR_NEW_ID));
    }

    @Test
    @DisplayName("обновлять все поля автора по id")
    void updateById() {
        assertThatExceptionOfType(DomainNotFound.class)
                .isThrownBy(() -> genreDao.updateById(AUTHOR_NEW_ID, new Author(AUTHOR_CORRECT_NAME)));

    }

    @Test
    @DisplayName("выдавать ошибку, когда автора по id нет")
    void updateById_authorNotExists() {
        Author authorExpected = new Author(AUTHOR_CORRECT_ID, AUTHOR_NEW_CORRECT_NAME);
        Author authorBefore = new Author(AUTHOR_CORRECT_ID, AUTHOR_CORRECT_NAME);

        genreDao.updateById(authorBefore.getId(), authorExpected);

        assertThat(genreDao.getById(authorBefore.getId()))
                .isEqualTo(authorExpected);

    }

    @Test
    @DisplayName("корректно сохранять автора")
    void save() {
        Author authorExpected = new Author(AUTHOR_NEW_ID, AUTHOR_NEW_NAME);

        assertThat(genreDao.save(new Author(AUTHOR_NEW_NAME)))
                .isEqualTo(authorExpected.getId());

        assertThat(genreDao.getAll())
                .hasSize(AUTHORS_BEFORE_SIZE + 1)
                .contains(authorExpected);

    }

    @Test
    @DisplayName("получать всех авторов")
    void getAll() {
        assertThat(genreDao.getAll())
                .hasSize(AUTHORS_BEFORE_SIZE)
                .contains(new Author(AUTHOR_CORRECT_FOR_REMOVE_ID, AUTHOR_CORRECT_FOR_REMOVE_NAME));
    }

    @Test
    @DisplayName("не удалять автора по id, если есть связи с книгами")
    void deleteById_ifBookRelation() {
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> genreDao.deleteById(AUTHOR_NOT_CORRECT_FOR_REMOVE_ID));
    }

    @Test
    @DisplayName("удалять автора по id")
    void deleteById() {
        genreDao.deleteById(AUTHOR_CORRECT_FOR_REMOVE_ID);

        assertThat(genreDao.getAll())
                .hasSize(AUTHORS_BEFORE_SIZE - 1)
                .doesNotContain(new Author(AUTHOR_CORRECT_FOR_REMOVE_ID, AUTHOR_CORRECT_FOR_REMOVE_NAME));

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> genreDao.getById(AUTHOR_CORRECT_FOR_REMOVE_ID));
    }
}