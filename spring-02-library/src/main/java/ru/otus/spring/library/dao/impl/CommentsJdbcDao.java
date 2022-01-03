package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@DaoRepository
@RequiredArgsConstructor
public class CommentsJdbcDao implements Dao<Long, Comment> {

    private final EntityManager em;

    @Override
    public Optional<Comment> getById(Long id) {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c JOIN FETCH c.book where c.id = :id", Comment.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Comment save(Comment entity) {
        if(entity.getId() == null || entity.getId() == 0){
            em.persist(entity);
            return entity;
        }
        return em.merge(entity);
    }

    @Override
    public List<Comment> getAll() {
        TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c JOIN FETCH c.book", Comment.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        getById(id).ifPresent(em::remove);
    }
}
