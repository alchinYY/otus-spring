package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService implements EntityService<Genre> {

    private final Dao<Long, Genre> genreDao;

    @Override
    @Transactional(readOnly = true)
    public Genre getById(long id){
        return genreDao.getById(id).orElseThrow(() -> new DomainNotFound("genre"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> getAll(){
        return genreDao.getAll();
    }

    @Override
    @Transactional
    public Long save(Genre genre){
        return genreDao.save(genre).getId();
    }

    @Override
    @Transactional
    public Genre updateById(Long id, Genre genre){
        genreDao.updateById(id, genre);
        return getById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        genreDao.deleteById(id);
    }

}
