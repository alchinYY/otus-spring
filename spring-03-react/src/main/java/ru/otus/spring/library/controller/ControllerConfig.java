package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Component
@RequiredArgsConstructor
public class ControllerConfig {

    private final RouterFunction<ServerResponse> genreRoutes;
    private final RouterFunction<ServerResponse> authorRoutes;
    private final RouterFunction<ServerResponse> bookRoutes;

    @Bean
    public RouterFunction<ServerResponse> genreRoutes() {
        return route()
                .add(genreRoutes)
                .add(authorRoutes)
                .add(bookRoutes)
                .build();
    }

}
