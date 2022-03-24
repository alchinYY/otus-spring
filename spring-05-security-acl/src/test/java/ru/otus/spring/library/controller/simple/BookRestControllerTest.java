package ru.otus.spring.library.controller.simple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookRestController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    void deleteTest() throws Exception {
        doNothing().when(bookService).deleteById(BOOK_REMOVE_ID_TEST);

        mvc.perform(delete(BookRestController.BOOK_URL + "/{id}", BOOK_REMOVE_ID_TEST))
                .andExpect(status().isOk());
    }

    @Test
    void createBook() throws Exception {
        BookDto bookDtoBefore = new BookDto(null, "name1", new GenreDto(1L,"g1"), List.of());
        BookDto bookDtoAfter = new BookDto(1L, "name1", new GenreDto(1L,"g1"), List.of());

        Book bookBefore = new Book(null, "name1", Set.of(), new Genre(1L,"g1"), List.of());
        Book bookAfter = new Book(1L, "name1", Set.of(), new Genre(1L,"g1"), List.of());

        given(modelMapper.map(bookDtoBefore, Book.class)).willReturn(bookBefore);
        given(modelMapper.map(bookAfter, BookDto.class)).willReturn(bookDtoAfter);
        given(bookService.save(bookBefore)).willReturn(bookAfter);

        mvc.perform(post(BookRestController.BOOK_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookBefore)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDtoAfter)));
    }

    @Test
    void getBookById() throws Exception {
        BookDto bookDto = new BookDto(1L, "name1", new GenreDto(1L,"g1"), List.of());
        Book book = new Book(1L, "name1", Set.of(), new Genre(1L,"g1"), List.of());

        given(bookService.getById(book.getId())).willReturn(book);
        given(modelMapper.map(book, BookDto.class)).willReturn(bookDto);

        mvc.perform(get(BookRestController.BOOK_URL + "/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(bookDto)));
    }
}