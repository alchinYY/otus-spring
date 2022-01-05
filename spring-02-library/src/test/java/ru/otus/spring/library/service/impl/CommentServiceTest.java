package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import ru.otus.spring.library.dao.impl.CommentsJdbcDao;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private CommentsJdbcDao commentDao;

    @Test
    void getById() {
        Comment comment = mock(Comment.class);
        when(commentDao.getById(anyLong())).thenReturn(Optional.of(comment));

        assertThat(commentService.getById(COMMENT_ID))
                .isEqualTo(comment);

        verify(commentDao, times(1)).getById(anyLong());

    }

    @Test
    void getAll() {
        List<Comment> commentList = List.of(mock(Comment.class));
        when(commentDao.getAll()).thenReturn(commentList);

        assertThat(commentService.getAll())
                .isEqualTo(commentList);

        verify(commentDao, times(1)).getAll();
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

        when(commentDao.save(any()))
                .thenReturn(commentAfter);


        assertThat(commentService.save(commentBefore, 1L))
                .isEqualTo(commentAfter);
        verify(commentDao, times(1)).save(any());
    }

    @Test
    void updateById() {
        Comment commentAfter = Comment.builder()
                .body("body new")
                .id(1L)
                .build();

        when(commentDao.getById(commentAfter.getId())).thenReturn(Optional.of(commentAfter));

        assertThat(commentService.updateById(commentAfter.getId(), commentAfter))
                .isEqualTo(commentAfter);
    }

    @Test
    void deleteById() {
        doNothing().when(commentDao).deleteById(anyLong());
        commentService.deleteById(COMMENT_ID);
        verify(commentDao, times(1)).deleteById(any());
    }
}