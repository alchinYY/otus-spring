package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@DaoRepository
public class BookJdbcDao implements Dao<Long, Book> {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> getById(Long id) {
        TypedQuery<Book> query = em.createQuery(
                "SELECT b FROM Book b LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.genre WHERE b.id = :id",
                Book.class
        );
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public void updateById(Long id, Book entity) {
        entity.setId(id);
        System.out.println(entity);
        em.merge(entity);
    }

    @Override
    public Book save(final Book entity) {
        if (entity.getId() == null || entity.getId() == 0) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery(
                "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.genre JOIN FETCH b.authors", Book.class
        );
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        getById(id).ifPresent(em::remove);
    }
}
