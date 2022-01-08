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
        return Optional.ofNullable(em.find(Book.class, id));
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
                "SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.genre JOIN FETCH b.authors LEFT JOIN FETCH b.comments", Book.class
        );
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        getById(id).ifPresent(em::remove);
    }
}
