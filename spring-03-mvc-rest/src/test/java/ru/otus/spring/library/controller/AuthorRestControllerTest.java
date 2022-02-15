package ru.otus.spring.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.service.impl.AuthorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthorRestController.class)
class AuthorRestControllerTest {

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAuthors() throws Exception {
        List<Author> authors = List.of(new Author(1L, "a1"), new Author(2L, "a2"));

        AuthorDto authorDto1 = new AuthorDto(authors.get(0).getId(), authors.get(0).getName());
        AuthorDto authorDto2 = new AuthorDto(authors.get(1).getId(), authors.get(1).getName());

        given(authorService.getAll()).willReturn(authors);
        given(modelMapper.map(any(), eq(AuthorDto.class)))
                .willReturn(authorDto1, authorDto2);

        mvc.perform(get(AuthorRestController.AUTHOR_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(authorDto1, authorDto2))));

    }
}