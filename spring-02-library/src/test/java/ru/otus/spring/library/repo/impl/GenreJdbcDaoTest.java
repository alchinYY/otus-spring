package ru.otus.spring.library.repo.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.transaction.AfterTransaction;
import ru.otus.spring.library.domain.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с жанрами должно")
@DataJpaTest
class GenreJdbcDaoTest {

    private static final int GENRES_BEFORE_SIZE = 4;
    private static final Long GENRE_CORRECT_FOR_REMOVE_ID = 4L;
    private static final String GENRE_CORRECT_FOR_REMOVE_NAME = "Романтика";
    private static final String GENRE_CORRECT_NAME = "роман-эпопея";
    private static final Long GENRE_CORRECT_ID = 1L;

    private static final Long GENRE_NEW_ID = 5L;
    private static final String GENRE_NEW_NAME = "Жанр не для книг";

    @Autowired
    private JpaRepository<Genre, Long> genreRepository;

    @Autowired
    private TestEntityManager em;

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

        assertThat(genreRepository.findById(genreExpected.getId()))
                .isNotEmpty()
                .get()
                .isEqualTo(genreExpected);
    }

    @Test
    @DisplayName("возвращать empty, если жанр по id нет")
    void getById_idNotFound() {
        assertThat(genreRepository.findById(GENRE_NEW_ID))
                .isEmpty();
    }

    @Test
    @DisplayName("обновлять все поля жанр по id")
    void updateById() {
        Genre genre = Genre.builder()
                .id(GENRE_CORRECT_ID)
                .name(GENRE_CORRECT_NAME)
                .build();

        genreRepository.save(genre);
        Genre authorAfterUpdate = em.find(Genre.class, GENRE_CORRECT_ID);

        assertThat(authorAfterUpdate)
                .hasFieldOrPropertyWithValue("name", genre.getName())
                .hasFieldOrPropertyWithValue("id", GENRE_CORRECT_ID);

    }

    @Test
    @DisplayName("корректно сохранять жанр")
    void save() {
        Genre genreExpected = new Genre(GENRE_NEW_ID, GENRE_NEW_NAME);

        assertThat(genreRepository.save(new Genre(GENRE_NEW_NAME)))
                .isEqualTo(genreExpected);

        assertThat(genreRepository.findAll())
                .hasSize(GENRES_BEFORE_SIZE + 1)
                .contains(genreExpected);
    }

    @Test
    @DisplayName("получать всех жанры")
    void getAll() {
        assertThat(genreRepository.findAll())
                .hasSize(GENRES_BEFORE_SIZE)
                .contains(new Genre(GENRE_CORRECT_FOR_REMOVE_ID, GENRE_CORRECT_FOR_REMOVE_NAME));
    }


    @Test
    @DisplayName("удалять жанр по id")
    void deleteById() {
        genreRepository.deleteById(GENRE_CORRECT_FOR_REMOVE_ID);

        assertThat(genreRepository.findAll())
                .hasSize(GENRES_BEFORE_SIZE - 1)
                .doesNotContain(new Genre(GENRE_CORRECT_FOR_REMOVE_ID, GENRE_CORRECT_FOR_REMOVE_NAME));

        assertThat(genreRepository.findById(GENRE_CORRECT_FOR_REMOVE_ID))
                .isEmpty();
    }
}