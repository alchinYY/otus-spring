package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.exception.DomainNotFound;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final JpaRepository<Comment, Long> commentRepository;
    private final BookService bookService;

    public Comment getById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("comment"));
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public List<Comment> getAllByBookId(Long id) {
        return bookService.getById(id).getComments();
    }

    @Transactional
    public Comment save(Comment comment, Long bookId) {
        bookService.getById(bookId)
                .getComments()
                .add(comment);
        return comment;
    }

    @Transactional
    public Comment updateById(Long id, Comment comment) {
        Comment commentFromDb = getById(id);
        commentFromDb.setBody(comment.getBody());
        return commentFromDb;
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
