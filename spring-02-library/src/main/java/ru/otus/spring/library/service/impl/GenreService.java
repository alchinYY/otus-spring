package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService implements EntityService<Genre> {

    private final Dao<Long, Genre> genreDao;

    @Override
    public Genre getById(long id){
        return genreDao.getById(id);
    }
    @Override
    public List<Genre> getAll(){
        return genreDao.getAll();
    }

    @Override
    public Long save(Genre genre){
        return genreDao.save(genre);
    }

    @Override
    public Genre updateById(Long id, Genre genre){
        genreDao.updateById(id, genre);
        return genreDao.getById(id);
    }

    @Override
    public void deleteById(Long id){
        genreDao.deleteById(id);
    }

}
