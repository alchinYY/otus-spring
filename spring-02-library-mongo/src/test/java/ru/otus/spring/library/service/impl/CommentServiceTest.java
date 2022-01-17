package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.repo.CommentRepository;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import({CommentService.class})
class CommentServiceTest {

    private final static int COMMENT_ID = 1;

    @Autowired
    private CommentService commentService;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private SequenceGeneratorService<Integer> sequenceGeneratorService;

    @Configuration
    public static class BookServiceTestConf{}

    @Test
    void getById() {
        Comment comment = mock(Comment.class);
        when(commentRepository.findById(anyInt())).thenReturn(Optional.of(comment));

        assertThat(commentService.getById(COMMENT_ID)).isEqualTo(comment);

        verify(commentRepository, times(1)).findById(anyInt());

    }

    @Test
    void getAll() {
        List<Comment> commentList = List.of(mock(Comment.class));
        when(commentRepository.findAll()).thenReturn(commentList);

        assertThat(commentService.getAll()).isEqualTo(commentList);

        verify(commentRepository, times(1)).findAll();
    }

    @Test
    void getAllByBookId() {
        List<Comment> comments = List.of(mock(Comment.class));
        Book book = Book.builder()
                .comments(comments)
                .build();

        when(bookService.getById(anyInt())).thenReturn(book);
        assertThat(commentService.getAllByBookId(1)).isEqualTo(comments);

        verify(bookService, times(1)).getById(anyInt());
    }

    @Test
    void save() {
        int newCommentId = 1;
        Comment commentBefore = new Comment("body");
        Comment commentAfter = new Comment(newCommentId, "body");

        when(sequenceGeneratorService.generateSequence(any())).thenReturn(newCommentId);

        when(commentRepository.save(any())).thenReturn(commentAfter);

        Book book = new Book();
        book.setComments(new ArrayList<>());

        when(bookService.getById(anyInt())).thenReturn(book);
        commentBefore.setId(commentAfter.getId());
        assertThat(commentService.save(commentBefore, newCommentId)).isEqualTo(commentAfter);
        verify(bookService, times(1)).getById(anyInt());
        verify(sequenceGeneratorService, times(1)).generateSequence(any());
    }

    @Test
    void updateById() {
        int commentId = 1;
        Comment commentAfter = new Comment(commentId, "body new");
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(commentAfter));
        when(commentRepository.save(commentAfter)).thenReturn(commentAfter);

        assertThat(commentService.updateById(commentId, commentAfter)).isEqualTo(commentAfter);
        verify(commentRepository, times(1)).save(any());
        verify(commentRepository, times(1)).findById(any());
    }

    @Test
    void deleteById() {
        doNothing().when(commentRepository).deleteById(anyInt());
        commentService.deleteById(COMMENT_ID);
        verify(commentRepository, times(1)).deleteById(any());
    }
}