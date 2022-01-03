package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.dao.impl.CommentsJdbcDao;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityService<Comment> {

    private final CommentsJdbcDao commentDao;
    private final BookService bookService;

    @Override
    public Comment getById(long id) {
        return commentDao.getById(id)
                .orElseThrow(() -> new DomainNotFound("comment"));
    }

    @Override
    public List<Comment> getAll() {
        return commentDao.getAll();
    }

    @Transactional(readOnly = true)
    public Set<Comment> getAllByBookId(Long id) {
        return bookService.getById(id).getComments();
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        comment.setBook(bookService.getById(comment.getBook().getId()));
        return commentDao.save(comment);
    }

    @Override
    @Transactional
    public Comment updateById(Long id, Comment comment) {
        Comment commentFromDb = getById(id);
        commentFromDb.setBody(comment.getBody());
        return commentDao.save(commentFromDb);
    }

    @Override
    public void deleteById(Long id) {
        commentDao.deleteById(id);
    }
}
