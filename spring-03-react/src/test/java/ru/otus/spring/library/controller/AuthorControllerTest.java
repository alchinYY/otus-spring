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
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.repository.AuthorRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@WebFluxTest({ AuthorController.class })
class AuthorControllerTest {

    @Autowired
    private RouterFunction<ServerResponse> authorRoutes;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void getAuthorList() {

        Author author1 = new Author("id1", "a1");
        Author author2 = new Author("id2", "a2");

        AuthorDto authorDto1 = new AuthorDto(author1.getId(), author1.getName());
        AuthorDto authorDto2 = new AuthorDto(author2.getId(), author2.getName());

        given(authorRepository.findAll()).willReturn(Flux.just(author1, author2));
        given(modelMapper.map(any(), eq(AuthorDto.class))).willReturn(authorDto1, authorDto2);

        WebTestClient client = WebTestClient.bindToRouterFunction(authorRoutes).build();

        client.get()
                .uri(AuthorController.AUTHOR_URL)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                    .isOk()
                .expectBodyList(AuthorDto.class)
                    .contains(authorDto1, authorDto2);
    }
}