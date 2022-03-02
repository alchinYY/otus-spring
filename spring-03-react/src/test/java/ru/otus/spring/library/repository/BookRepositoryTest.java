package ru.otus.spring.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.spring.library.domain.Book;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class BookRepositoryTest {

    private static final String BOOK_1_ID = "id1";
    private static final String BOOK_2_ID = "id2";

    private static final String BOOK_1_NAME = "book1";
    private static final String BOOK_2_NAME = "book2";

    private static final String AUTHOR_ID_BOOK_1 = "authorId1";
    private static final String AUTHOR_ID_BOOK_2 = "authorId2";

    private static final String GENRE_ID_BOOK = "genreId";

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAll() {
        Book book1 = createBookId1();

        Book book2 = Book.builder()
                .id(BOOK_2_ID)
                .name(BOOK_2_NAME)
                .genre(GENRE_ID_BOOK)
                .authors(List.of(AUTHOR_ID_BOOK_1, AUTHOR_ID_BOOK_2))
                .build();

        Flux<Book> bookFlux = Flux.just(book1, book2)
                .flatMap(this.bookRepository::save)
                .thenMany(bookRepository.findAll())
                .sort(Comparator.comparing(Book::getId));

        StepVerifier
                .create(bookFlux)
                .expectNextMatches(book -> book.equals(book1))
                .expectNextMatches(book -> book.equals(book2))
                .verifyComplete();
    }

    @Test
    void save() {
        Book book = createBookId1();

        Mono<Book> bookMono = bookRepository.save(book);

        StepVerifier
                .create(bookMono)
                .assertNext(b -> assertEquals(b, book))
                .verifyComplete();

    }

    private Book createBookId1() {
        return Book.builder()
                .id(BOOK_1_ID)
                .name(BOOK_1_NAME)
                .genre(GENRE_ID_BOOK)
                .authors(List.of(AUTHOR_ID_BOOK_1, AUTHOR_ID_BOOK_2))
                .build();
    }
}