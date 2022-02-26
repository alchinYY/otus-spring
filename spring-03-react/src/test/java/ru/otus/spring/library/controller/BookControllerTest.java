package ru.otus.spring.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.handler.BookRepoHandler;

import java.util.List;

import static org.mockito.BDDMockito.given;

@WebFluxTest({ BookController.class })
class BookControllerTest {

    private static final String BOOK_1_ID = "id1";
    private static final String BOOK_2_ID = "id2";

    private static final String BOOK_1_NAME = "book1";
    private static final String BOOK_2_NAME = "book2";

    private static final String AUTHOR_ID_BOOK_1 = "authorId1";
    private static final String AUTHOR_ID_BOOK_2 = "authorId2";

    private static final String GENRE_ID_BOOK = "genreId";

    @Autowired
    private RouterFunction<ServerResponse> bookRoutes;

    @MockBean
    private BookRepoHandler bookRepoHandler;

    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToRouterFunction(bookRoutes).build();
    }

    @Test
    void getBookList() {

        BookDto bookDto1 = new BookDto(BOOK_1_ID, BOOK_1_NAME);
        BookDto bookDto2 = new BookDto(BOOK_2_ID, BOOK_2_NAME);

        given(bookRepoHandler.getAllDto()).willReturn(Mono.just(List.of(bookDto1, bookDto2)));

        client.get()
                .uri(BookController.BOOK_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBodyList(BookDto.class)
                    .contains(bookDto1, bookDto2);
    }

    @Test
    void deleteBookById() {
        String testId = "id1";
        given(bookRepoHandler.delete(testId)).willReturn(Mono.empty());

        client.delete()
                .uri(BookController.BOOK_URL + "/{id}", testId)
                    .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBody(BookDto.class);
    }

    @Test
    void createBook() {
        BookDto bookDtoBeforeSave = new BookDto(null, BOOK_1_NAME);
        BookDto bookDtoAfterSave = new BookDto(BOOK_1_ID, BOOK_1_NAME);
        given(bookRepoHandler.save(bookDtoBeforeSave)).willReturn(Mono.just(bookDtoAfterSave));


        client.post()
                .uri(BookController.BOOK_URL)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookDtoBeforeSave), BookDto.class)
                .exchange()
                .expectStatus()
                    .isCreated()
                .expectBody(BookDto.class)
                    .isEqualTo(bookDtoAfterSave);
    }
}