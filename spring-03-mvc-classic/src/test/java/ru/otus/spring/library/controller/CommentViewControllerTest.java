package ru.otus.spring.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.CommentDto;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.service.impl.BookService;
import ru.otus.spring.library.service.impl.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentViewController.class)
class CommentViewControllerTest {

    private static final String BOOK_NAME_TEST = "name";
    private static final Long BOOK_ID_TEST = 1L;

    private static final Long COMMENT_ID_TEST_1 = 1L;
    private static final Long COMMENT_ID_TEST_2 = 2L;

    private static final String COMMENT_BODY_TEST_1 = "body1";
    private static final String COMMENT_BODY_TEST_2 = "body2";

    private static final String URL_GET_COMMENT = "/library/book/{id}/comments";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CommentService commentService;
    @MockBean
    private BookService bookService;
    @MockBean
    private ModelMapper modelMapper;

    @Test
    void getCommentsByBookId() throws Exception {
        List<Comment> commentList = List.of(
                new Comment(COMMENT_ID_TEST_1, LocalDateTime.now(), COMMENT_BODY_TEST_1),
                new Comment(COMMENT_ID_TEST_2, LocalDateTime.now(), COMMENT_BODY_TEST_2)
        );

        Book book = new Book(BOOK_ID_TEST, BOOK_NAME_TEST);
        book.getComments().addAll(commentList);

        given(bookService.getById(book.getId())).willReturn(book);

        given(modelMapper.map(any(), eq(BookDto.class)))
                .willReturn(new BookDto(book.getId(), book.getName(), new GenreDto(), List.of()));
        given(modelMapper.map(commentList.get(0), CommentDto.class))
                .willReturn(new CommentDto(commentList.get(0).getDate(), commentList.get(0).getBody()));
        given(modelMapper.map(commentList.get(1), CommentDto.class))
                .willReturn(new CommentDto(commentList.get(1).getDate(), commentList.get(1).getBody()));

        mvc.perform(get(URL_GET_COMMENT, book.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(commentList.get(0).getBody())))
                .andExpect(content().string(containsString(commentList.get(1).getBody())));
    }

    @Test
    void addComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setBody("body test comment");
        comment.setDate(LocalDateTime.now());
        given(commentService.save(any(), any()))
                .willReturn(comment);

        Book book = new Book(BOOK_ID_TEST, BOOK_NAME_TEST);
        book.getComments().add(comment);

        given(bookService.getById(book.getId())).willReturn(book);
        given(modelMapper.map(any(), eq(BookDto.class)))
                .willReturn(new BookDto(book.getId(), book.getName(), new GenreDto(), List.of()));
        given(modelMapper.map(comment, CommentDto.class))
                .willReturn(new CommentDto(comment.getDate(), comment.getBody()));


        mvc.perform(
                post(URL_GET_COMMENT, book.getId())
                        .param("body",  comment.getBody()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(comment.getBody())))
                .andExpect(content().string(containsString(comment.getDate().toString())));
    }

    @Test
    void addComment_notBlankException() throws Exception {
        Comment comment = new Comment();
        comment.setBody("");
        mvc.perform(
                post(URL_GET_COMMENT, BOOK_ID_TEST)
                        .param("body",  comment.getBody()))
                .andExpect(content().string(containsString("Body field should not be blank")));
    }
}