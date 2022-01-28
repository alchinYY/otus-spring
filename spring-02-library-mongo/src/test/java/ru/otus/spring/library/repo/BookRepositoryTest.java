package ru.otus.spring.library.repo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.library.domain.Book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestRepoConfiguration.class)
class BookRepositoryTest {

    private static final int TEST_COMMENT_ID = 1;

    private static final String TOLKIEN_AUTHOR = "Толкин, Джон Рональд Руэл";
    private static final int TOLKIEN_AUTHOR_ID = 1;
    private static final int NOT_CORRECT_AUTHOR_ID = 100;

    private static final String HOBBIT_BOOK = "Хоббит. Туда и обратно";
    private static final String LOTR_BOOK = "Властелин колец";
    private static final String TALE_OF_TSAR_SALTAN_BOOK = "Сказка о царе Салтане";
    private static final String COLLECTION_OF_STRUGATSKY_BOOK = "За миллиард лет до конца света.(сборник)";

    private static final String EPIC_NOVEL_GENRE = "роман-эпопея";
    private static final int EPIC_NOVEL_GENRE_ID = 1;

    private static final String TALE_GENRE = "сказка";
    private static final int TALE_GENRE_ID = 2;

    private static final String SINCE_FICTION_GENRE = "научная фантастика";
    private static final int SINCE_FICTION_GENRE_ID = 3;


    private static final int HOBBIT_BOOK_ID = 1;

    @Autowired
    private BookRepository bookRepository;

    @DisplayName("Должен вернуть все книги по жанру")
    @Test
    void findByGenreId() {
        assertThat(bookRepository.findByGenreId(EPIC_NOVEL_GENRE_ID))
                .hasSize(2)
                .extracting(Book::getName)
                .containsExactly(HOBBIT_BOOK, LOTR_BOOK);
    }

    @Test
    void findByAuthorsId() {
        assertThat(bookRepository.findByAuthorsId(TOLKIEN_AUTHOR_ID))
                .hasSize(2)
                .extracting(Book::getName)
                .containsExactly(HOBBIT_BOOK, LOTR_BOOK);
    }

    @Test
    void findByAuthorsId_isEmpty() {
        assertThat(bookRepository.findByAuthorsId(NOT_CORRECT_AUTHOR_ID))
                .isEmpty();
    }

    @Test
    void findByCommentsId() {
        assertThat(bookRepository.findByCommentsId(TEST_COMMENT_ID))
                .isNotEmpty()
                .get()
                .extracting(Book::getName, Book::getId)
                .containsExactly(HOBBIT_BOOK, HOBBIT_BOOK_ID);

    }

    @Test
    void findAll(){
        assertThat(bookRepository.findAll())
                .hasSize(4)
                .extracting(Book::getName, b -> b.getGenre().getId())
                .containsExactly(
                        tuple(HOBBIT_BOOK, EPIC_NOVEL_GENRE_ID),
                        tuple(LOTR_BOOK, EPIC_NOVEL_GENRE_ID),
                        tuple( TALE_OF_TSAR_SALTAN_BOOK, TALE_GENRE_ID),
                        tuple(COLLECTION_OF_STRUGATSKY_BOOK, SINCE_FICTION_GENRE_ID)
                );
    }
}