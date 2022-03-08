package ru.otus.spring.library.controller.secure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.controller.BookRestController;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.service.impl.BookService;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = BookRestController.class)
class BookRestControllerTest {

    private static final long BOOK_REMOVE_ID_TEST = 1L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser(
            username = "user1"
    )
    void getAllBook() throws Exception {
        Book book1 = new Book(1L, "name1", Set.of(), new Genre(1L,"g1"), List.of());
        Book book2 = new Book(2L, "name2", Set.of(), new Genre(2L, "g2"), List.of());

        BookDto bookDto1 = new BookDto(1L, "name1", new GenreDto(1L,"g1"), List.of());
        BookDto bookDto2 = new BookDto(2L, "name2", new GenreDto(2L, "g2"), List.of());

        given(bookService.getAll()).willReturn(List.of(book1, book2));
        given(modelMapper.map(any(), eq(BookDto.class))).willReturn(bookDto1, bookDto2);

        mvc.perform(get(BookRestController.BOOK_URL))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bookDto1, bookDto2))));
    }

    @Test
    @WithMockUser(
            username = "user1"
    )
    void deleteTest() throws Exception {
        mvc.perform(delete(BookRestController.BOOK_URL + "/{id}", BOOK_REMOVE_ID_TEST))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTest_unauthorized() throws Exception {
        mvc.perform(delete(BookRestController.BOOK_URL + "/{id}", BOOK_REMOVE_ID_TEST))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @Test
    @WithMockUser(
            username = "user1"
    )
    void createBook() throws Exception {
        Book bookBefore = new Book(null, "name1", Set.of(), new Genre(1L,"g1"), List.of());

        mvc.perform(post(BookRestController.BOOK_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookBefore)))
                .andExpect(status().isOk());
    }

    @Test
    void createBook_unauthorized() throws Exception {

        mvc.perform(post(BookRestController.BOOK_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Strings.EMPTY)))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", "http://localhost/login"));
    }

    @Test
    @WithMockUser(
            username = "user1"
    )
    void getBookById() throws Exception {
        mvc.perform(get(BookRestController.BOOK_URL + "/{id}", 1))
                .andExpect(status().isOk());
    }


    @Test
    void getBookById_unauthorized() throws Exception {
        mvc.perform(get(BookRestController.BOOK_URL + "/{id}", 1))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", "http://localhost/login"));
    }
}