package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.handler.BookRepoHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Component
@RequiredArgsConstructor
public class BookController {

    static final String BOOK_URL = "/library/book";

    private final BookRepoHandler bookHandler;

    @Bean
    public RouterFunction<ServerResponse> bookRoutes() {
        return route()
                .GET(BOOK_URL, accept(APPLICATION_JSON),
                        request -> bookHandler.getAllDto()
                                .flatMap(books -> ok().contentType(APPLICATION_JSON).body(fromValue(books)))
                )
                .DELETE(BOOK_URL + "/{id}", accept(APPLICATION_JSON),
                        request -> bookHandler.delete(request.pathVariable("id"))
                                .flatMap(nothing -> ok().contentType(APPLICATION_JSON).build())
                )
                .POST(BOOK_URL, accept(APPLICATION_JSON),
                        request -> request.bodyToMono(BookDto.class)
                                .flatMap(bookHandler::save)
                                .flatMap(book -> status(HttpStatus.CREATED)
                                        .contentType(APPLICATION_JSON)
                                        .body(fromValue(book))
                                )
                )
                .build();
    }
}


