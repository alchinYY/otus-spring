package ru.otus.spring.library.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.transaction.AfterTransaction;
import ru.otus.spring.library.domain.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Dao для работы с авторами должно")
@DataJpaTest
class AuthorRepoTest {

    private static final int AUTHORS_BEFORE_SIZE = 5;
    private static final long AUTHOR_CORRECT_FOR_REMOVE_ID = 5L;
    private static final String AUTHOR_CORRECT_FOR_REMOVE_NAME = "Неизвестный автор";
    private static final String AUTHOR_CORRECT_NAME = "Толкин, Джон Рональд Руэл";
    private static final Long AUTHOR_CORRECT_ID = 1L;

    private static final long AUTHOR_NEW_ID = 6L;
    private static final String AUTHOR_NEW_NAME = "Книжек еще не написал";

    @Autowired
    private JpaRepository<Author, Long> authorRepository;

    @Autowired
    private TestEntityManager em;

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

        assertThat(authorRepository.findById(authorExpected.getId()))
                .isNotEmpty()
                .get()
                .isEqualTo(authorExpected);
    }

    @Test
    @DisplayName("возвращать пустой ответ, если автора по id нет")
    void getById_idNotFound() {
        assertThat(authorRepository.findById(AUTHOR_NEW_ID))
                .isEmpty();
    }

    @Test
    @DisplayName("обновлять все поля автора по id")
    void updateById() {
        Author author = Author.builder()
                .id(AUTHOR_CORRECT_ID)
                .name(AUTHOR_CORRECT_NAME)
                .build(); new Author(AUTHOR_CORRECT_NAME);

        Author authorAfterUpdate = em.find(Author.class, AUTHOR_CORRECT_ID);

        assertThat(authorAfterUpdate)
                .hasFieldOrPropertyWithValue("name", author.getName())
                .hasFieldOrPropertyWithValue("id", AUTHOR_CORRECT_ID);
    }

    @Test
    @DisplayName("корректно сохранять автора")
    void save() {
        Author authorExpected = new Author(AUTHOR_NEW_ID, AUTHOR_NEW_NAME);

        assertThat(authorRepository.save(new Author(AUTHOR_NEW_NAME)))
                .isEqualTo(authorExpected);

        assertThat(authorRepository.findAll())
                .hasSize(AUTHORS_BEFORE_SIZE + 1)
                .contains(authorExpected);

    }

    @Test
    @DisplayName("получать всех авторов")
    void getAll() {
        assertThat(authorRepository.findAll())
                .hasSize(AUTHORS_BEFORE_SIZE)
                .contains(new Author(AUTHOR_CORRECT_FOR_REMOVE_ID, AUTHOR_CORRECT_FOR_REMOVE_NAME));
    }

    @Test
    @DisplayName("удалять автора по id")
    void deleteById() {

        authorRepository.deleteById(AUTHOR_CORRECT_FOR_REMOVE_ID);

        assertThat(authorRepository.findAll())
                .hasSize(AUTHORS_BEFORE_SIZE - 1)
                .doesNotContain(new Author(AUTHOR_CORRECT_FOR_REMOVE_ID, AUTHOR_CORRECT_FOR_REMOVE_NAME));

        assertThat(authorRepository.findById(AUTHOR_NEW_ID))
                .isEmpty();
    }
}