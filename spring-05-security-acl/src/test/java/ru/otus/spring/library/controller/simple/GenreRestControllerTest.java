package ru.otus.spring.library.controller.simple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.controller.GenreRestController;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.service.impl.GenreService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GenreRestController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    void getAllGenres() throws Exception {

        List<Genre> genres = List.of(new Genre(1L, "g1"), new Genre(2L, "g2"));

        GenreDto genreDto1 = new GenreDto(genres.get(0).getId(), genres.get(0).getName());
        GenreDto genreDto2 = new GenreDto(genres.get(1).getId(), genres.get(1).getName());

        given(genreService.getAll()).willReturn(genres);
        given(modelMapper.map(any(), eq(GenreDto.class))).willReturn(genreDto1, genreDto2);

        mvc.perform(get(GenreRestController.GENRE_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(genreDto1, genreDto2))));

    }
}