package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.repository.AuthorRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class AuthorController {

    private static final String AUTHOR_URL = "/library/author";

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Bean
    public RouterFunction<ServerResponse> authorRoutes() {
        return route()
                .GET(AUTHOR_URL, accept(APPLICATION_JSON),
                        request -> authorRepository
                                .findAll()
                                .map(a -> modelMapper.map(a, AuthorDto.class))
                                .collectList()
                                .flatMap(authors -> ok().contentType(APPLICATION_JSON).body(fromValue(authors)))
                )
                .build();
    }

}
