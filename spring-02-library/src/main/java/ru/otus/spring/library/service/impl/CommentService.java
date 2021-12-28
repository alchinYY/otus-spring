package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.dao.impl.CommentsJdbcDao;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService implements EntityService<Comment> {

    private final CommentsJdbcDao commentDao;

    @Override
    @Transactional(readOnly = true)
    public Comment getById(long id) {
        return commentDao.getById(id)
                .orElseThrow(() -> new DomainNotFound("comment"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAll() {
        return commentDao.getAll();
    }

    @Transactional(readOnly = true)
    public List<Comment> getAllByBookId(Long id) {
        return commentDao.getByBookId(id);
    }


    @Override
    @Transactional
    public Long save(Comment comment) {
        return commentDao.save(comment).getId();
    }

    @Override
    @Transactional
    public Comment updateById(Long id, Comment comment) {
        commentDao.updateById(id, comment);
        return getById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        commentDao.deleteById(id);
    }
}
