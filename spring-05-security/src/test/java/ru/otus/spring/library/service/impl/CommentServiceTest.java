package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.repo.CommentRepo;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
@EnableAspectJAutoProxy
@Import({CommentService.class})
class CommentServiceTest {

    private final static Long COMMENT_ID = 1L;

    @Autowired
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentRepo commentRepository;

    @Test
    void getById() {
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        assertThat(commentService.getById(COMMENT_ID))
                .isEqualTo(comment);

        verify(commentRepository, times(1)).findById(anyLong());

    }

    @Test
    void getAll() {
        List<Comment> commentList = List.of(mock(Comment.class));
        when(commentRepository.findAll()).thenReturn(commentList);

        assertThat(commentService.getAll())
                .isEqualTo(commentList);

        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void getAllByBookId() {
        List<Comment> comments = List.of(mock(Comment.class));
        Book book = Book.builder()
                .comments(comments)
                .build();

        when(bookService.getById(anyLong())).thenReturn(book);
        assertThat(commentService.getAllByBookId(1L))
                .isEqualTo(comments);

        verify(bookService, times(1)).getById(anyLong());
    }

    @Test
    void save() {
        Comment commentBefore = Comment.builder()
                .body("body")
                .build();

        Comment commentAfter = Comment.builder()
                .body("body")
                .id(1L)
                .build();

        when(commentRepository.save(any()))
                .thenReturn(commentAfter);

        Book book = new Book();
        book.setComments(new ArrayList<>());
        when(bookService.getById(anyLong()))
                .thenReturn(book);
        commentBefore.setId(commentAfter.getId());
        assertThat(commentService.save(commentBefore, 1L))
                .isEqualTo(commentAfter);
        verify(bookService, times(1)).getById(anyLong());
    }

    @Test
    void updateById() {
        Comment commentAfter = Comment.builder()
                .body("body new")
                .id(1L)
                .build();

        when(commentRepository.findById(commentAfter.getId())).thenReturn(Optional.of(commentAfter));

        assertThat(commentService.updateById(commentAfter.getId(), commentAfter))
                .isEqualTo(commentAfter);
    }

    @Test
    void deleteById() {
        doNothing().when(commentRepository).deleteById(anyLong());
        commentService.deleteById(COMMENT_ID);
        verify(commentRepository, times(1)).deleteById(any());
    }
}