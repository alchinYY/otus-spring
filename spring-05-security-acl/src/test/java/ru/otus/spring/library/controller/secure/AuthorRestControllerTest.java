package ru.otus.spring.library.controller.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.controller.AuthorRestController;
import ru.otus.spring.library.service.impl.AuthorService;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthorRestController.class)
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
    @WithMockUser(
            username = "user1"
    )
    void getAuthors() throws Exception {
        mvc.perform(get(AuthorRestController.AUTHOR_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.EMPTY_LIST)));

    }

    @Test
    void getAuthors_unauthorized() throws Exception {
        mvc.perform(get(AuthorRestController.AUTHOR_URL))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", "http://localhost/login"));
    }
}