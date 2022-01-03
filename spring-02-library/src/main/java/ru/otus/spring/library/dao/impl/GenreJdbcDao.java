package ru.otus.spring.library.dao.impl;

import lombok.RequiredArgsConstructor;
import ru.otus.spring.library.aop.DaoRepository;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@DaoRepository
@RequiredArgsConstructor
public class GenreJdbcDao implements Dao<Long, Genre> {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Genre> getById(Long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Genre save(Genre entity) {
        if(entity.getId() == null || entity.getId() == 0){
            em.persist(entity);
            return entity;
        }
        return em.merge(entity);
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public void deleteById(Long id) {
        getById(id).ifPresent(em::remove);
    }
}
