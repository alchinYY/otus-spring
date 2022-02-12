package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityService<Comment> {

    private final JpaRepository<Comment, Long> commentRepository;
    private final BookService bookService;

    @Override
    public Comment getById(long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("comment"));
    }

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment save(Comment entity) {
        throw new UnsupportedOperationException();
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
    @Override
    public Comment updateById(Long id, Comment comment) {
        Comment commentFromDb = getById(id);
        commentFromDb.setBody(comment.getBody());
        return commentFromDb;
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
