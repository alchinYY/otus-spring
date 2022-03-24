package ru.otus.spring.library.controller.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.controller.GenreRestController;
import ru.otus.spring.library.service.impl.GenreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
class GenreRestControllerTest {

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private GenreService genreService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(
            username = "user1"
    )
    void getAllGenres() throws Exception {
        mvc.perform(get(GenreRestController.GENRE_URL))
                .andExpect(status().isOk());

    }

    @Test
    void getAllGenres_unauthorized() throws Exception {
        mvc.perform(get(GenreRestController.GENRE_URL))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", "http://localhost/login"));
    }
}