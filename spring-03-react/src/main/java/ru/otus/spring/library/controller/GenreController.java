package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.repository.GenreRepository;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RequiredArgsConstructor
public class GenreController {

    static final String GENRE_URL = "/library/genre";

    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    @Bean
    public RouterFunction<ServerResponse> genreRoutes() {
        return route()
                .GET(GENRE_URL, accept(APPLICATION_JSON),
                        response -> genreRepository.findAll()
                                .map(g -> modelMapper.map(g, GenreDto.class))
                                .collectList()
                                .flatMap(genres -> ok().contentType(APPLICATION_JSON).body(fromValue(genres)))
                )
                .build();
    }

}
