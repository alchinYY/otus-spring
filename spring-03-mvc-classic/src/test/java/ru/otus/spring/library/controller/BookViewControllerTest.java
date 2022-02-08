package ru.otus.spring.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.config.ModelMapperConfig;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.mapper.dto.BookEntityToDtoMapper;
import ru.otus.spring.library.service.impl.AuthorService;
import ru.otus.spring.library.service.impl.BookService;
import ru.otus.spring.library.service.impl.GenreService;

import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookViewController.class)
@Import({BookEntityToDtoMapper.class, ModelMapperConfig.class})
class BookViewControllerTest {

    private static final long BOOK_REMOVE_ID_TEST = 1L;
    private static final String NEW_BOOK_NAME = "new book name";
    private static final String BOOK_NAME_FOR_TEST = "book name for test";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BookEntityToDtoMapper bookEntityToDtoMapper;

    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void getAllBook() throws Exception {

        Book book1 = new Book(1L, "name1", Set.of(), new Genre(1L,"g1"), List.of());
        Book book2 = new Book(2L, "name2", Set.of(), new Genre(2L, "g2"), List.of());

        given(bookService.getAll())
                .willReturn(List.of(book1, book2));

        mvc.perform(get("/library/book"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(book1.getName())))
                .andExpect(content().string(containsString(book1.getGenre().getName())))
                .andExpect(content().string(containsString(book2.getName())))
                .andExpect(content().string(containsString(book2.getGenre().getName())));

    }

    @Test
    void delete() throws Exception {
        Book book2 = new Book(2L, "name2", Set.of(), new Genre(2L, "g2"), List.of());

        doNothing().when(bookService).deleteById(BOOK_REMOVE_ID_TEST);
        given(bookService.getAll())
                .willReturn(List.of(book2));

        mvc.perform(post("/library/book/delete").param("id", Long.toString(BOOK_REMOVE_ID_TEST)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/library/book"));

    }

    @Test
    void createFormForBook() throws Exception {
        List<Genre> genres = List.of(new Genre(1L, "g1"), new Genre(2L, "g2"));
        List<Author> authors = List.of(new Author(1L, "a1"), new Author(2L, "a2"));

        given(genreService.getAll()).willReturn(genres);

        given(authorService.getAll()).willReturn(authors);

        mvc.perform(get("/library/book/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(genres.get(0).getName())))
                .andExpect(content().string(containsString(genres.get(1).getName())))
                .andExpect(content().string(containsString(authors.get(0).getName())))
                .andExpect(content().string(containsString(authors.get(1).getName())));
    }

    @Test
    void createBook() throws Exception {

        given(bookService.save(any()))
                .willReturn(mock(Book.class));

        mvc.perform(
                post("/library/book/create")
                        .flashAttr("book", new BookDto(null, NEW_BOOK_NAME, new GenreDto(1L, "g1"), List.of()))
                        .param("authorsIds", "1", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/library/book"));


    }

    @Test
    void createBook_emptyGenre() throws Exception {
        mvc.perform(
                post("/library/book/create")
                        .param("name", "")
                        .param("authorsIds", "1", "2")
        )
                .andExpect(content().string(containsString("Genre should not be null")));

    }

    @Test
    void editFormGet() throws Exception {
        Book book = new Book(1L, BOOK_NAME_FOR_TEST, Set.of(), new Genre(1L,"g1"), List.of());
        List<Genre> genres = List.of(new Genre(1L, "g1"), new Genre(2L, "g2"));
        List<Author> authors = List.of(new Author(1L, "a1"), new Author(2L, "a2"));

        given(genreService.getAll()).willReturn(genres);
        given(authorService.getAll()).willReturn(authors);
        given(bookService.getById(book.getId())).willReturn(book);

        mvc.perform(get("/library/book/{id}", book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(book.getName())))
                .andExpect(content().string(containsString(genres.get(0).getName())))
                .andExpect(content().string(containsString(genres.get(1).getName())))
                .andExpect(content().string(containsString(authors.get(0).getName())))
                .andExpect(content().string(containsString(authors.get(1).getName())));
    }

    @Test
    void editFormPost() throws Exception {
        Book book = new Book(1L, BOOK_NAME_FOR_TEST, Set.of(), new Genre(1L,"g1"), List.of());
        given(bookService.updateById(book.getId(), book))
                .willReturn(book);

        mvc.perform(
                post("/library/book/{id}", book.getId())
                        .flashAttr("book", new BookDto(null, NEW_BOOK_NAME, new GenreDto(1L, "g1"), List.of()))
                        .param("authorsIds", "1", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/library/book"));
    }
}