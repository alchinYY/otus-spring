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
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.mappers.GenreMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Dao для работы с жанрами должно")
@JdbcTest
@Import({GenreJdbcDao.class, GenreMapper.class, AopDaoService.class})
class GenreJdbcDaoTest {

    private static final int GENRES_BEFORE_SIZE = 4;
    private static final Long GENRE_NOT_CORRECT_FOR_REMOVE_ID = 1L;
    private static final Long GENRE_CORRECT_FOR_REMOVE_ID = 4L;
    private static final String GENRE_CORRECT_FOR_REMOVE_NAME = "Романтика";
    private static final String GENRE_CORRECT_NAME = "роман-эпопея";
    private static final String GENRE_NEW_CORRECT_NAME = "фэнтези";
    private static final Long GENRE_CORRECT_ID = 1L;

    private static final Long GENRE_NEW_ID = 5L;
    private static final String GENRE_NEW_NAME = "Жанр не для книг";

    @Autowired
    private Dao<Long, Genre> genreDao;

    @Autowired
    private AopDaoService aopDaoService;

    @Autowired
    private GenreMapper genreMapper;
    

    @BeforeEach
    void setUp() {
    }

    @AfterTransaction
    void afterTransaction() {
    }

    @Test
    @DisplayName("возвращать ожидаемого жанр по id")
    void getById() {
        Genre genreExpected = new Genre(GENRE_CORRECT_ID, GENRE_CORRECT_NAME);

        assertThat(genreDao.getById(genreExpected.getId()))
                .isEqualTo(genreExpected);
    }

    @Test
    @DisplayName("возвращать ошибку, если жанр по id нет")
    void getById_idNotFound() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> genreDao.getById(GENRE_NEW_ID));
    }

    @Test
    @DisplayName("обновлять все поля жанр по id")
    void updateById() {
        assertThatExceptionOfType(DomainNotFound.class)
                .isThrownBy(() -> genreDao.updateById(GENRE_NEW_ID, new Genre(GENRE_CORRECT_NAME)));

    }

    @Test
    @DisplayName("выдавать ошибку, когда жанра по id нет")
    void updateById_genreNotExists() {
        Genre genreExpected = new Genre(GENRE_CORRECT_ID, GENRE_NEW_CORRECT_NAME);
        Genre genreBefore = new Genre(GENRE_CORRECT_ID, GENRE_CORRECT_NAME);

        genreDao.updateById(genreBefore.getId(), genreExpected);

        assertThat(genreDao.getById(genreBefore.getId()))
                .isEqualTo(genreExpected);

    }

    @Test
    @DisplayName("корректно сохранять жанр")
    void save() {
        Genre genreExpected = new Genre(GENRE_NEW_ID, GENRE_NEW_NAME);

        assertThat(genreDao.save(new Genre(GENRE_NEW_NAME)))
                .isEqualTo(genreExpected);

        assertThat(genreDao.getAll())
                .hasSize(GENRES_BEFORE_SIZE + 1)
                .contains(genreExpected);

    }

    @Test
    @DisplayName("получать всех жанры")
    void getAll() {
        assertThat(genreDao.getAll())
                .hasSize(GENRES_BEFORE_SIZE)
                .contains(new Genre(GENRE_CORRECT_FOR_REMOVE_ID, GENRE_CORRECT_FOR_REMOVE_NAME));
    }

    @Test
    @DisplayName("не удалять жанр по id, если есть связи с книгами")
    void deleteById_ifBookRelation() {
        assertThatExceptionOfType(DataIntegrityViolationException.class)
                .isThrownBy(() -> genreDao.deleteById(GENRE_NOT_CORRECT_FOR_REMOVE_ID));
    }

    @Test
    @DisplayName("удалять жанр по id")
    void deleteById() {
        genreDao.deleteById(GENRE_CORRECT_FOR_REMOVE_ID);

        assertThat(genreDao.getAll())
                .hasSize(GENRES_BEFORE_SIZE - 1)
                .doesNotContain(new Genre(GENRE_CORRECT_FOR_REMOVE_ID, GENRE_CORRECT_FOR_REMOVE_NAME));

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> genreDao.getById(GENRE_CORRECT_FOR_REMOVE_ID));
    }
}