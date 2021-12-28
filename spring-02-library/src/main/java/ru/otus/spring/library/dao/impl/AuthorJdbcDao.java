package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@DaoRepository
public class AuthorJdbcDao implements Dao<Long, Author> {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Author> getById(Long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public void updateById(Long id, Author entity) {
        entity.setId(id);
        em.merge(entity);
    }

    @Override
    public Author save(Author entity) {
        if (entity.getId() == null || entity.getId() == 0) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("SELECT a FROM Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        getById(id).ifPresent(em::remove);
    }
}
