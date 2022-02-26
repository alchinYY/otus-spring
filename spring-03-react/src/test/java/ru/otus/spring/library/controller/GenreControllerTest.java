package ru.otus.spring.library.controller;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.repository.GenreRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@WebFluxTest({ GenreController.class })
class GenreControllerTest {


    @Autowired
    private RouterFunction<ServerResponse> genreRoutes;

    @MockBean
    private GenreRepository genreRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void getGenreList() {

        Genre genre1 = new Genre("id1", "g1");
        Genre genre2 = new Genre("id2", "g2");

        GenreDto genreDto1 = new GenreDto(genre1.getId(), genre1.getName());
        GenreDto genreDto2 = new GenreDto(genre2.getId(), genre2.getName());

        given(genreRepository.findAll()).willReturn(Flux.just(genre1, genre2));
        given(modelMapper.map(any(), eq(GenreDto.class))).willReturn(genreDto1, genreDto2);

        WebTestClient client = WebTestClient.bindToRouterFunction(genreRoutes).build();

        client.get()
                .uri(GenreController.GENRE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBodyList(GenreDto.class)
                    .contains(genreDto1, genreDto2);

    }
}