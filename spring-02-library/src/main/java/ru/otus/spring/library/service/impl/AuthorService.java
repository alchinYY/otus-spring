package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorService implements EntityService<Author> {

    private final Dao<Long, Author> genreDao;

    @Override
    public Author getById(long id){
        return genreDao.getById(id);
    }

    @Override
    public List<Author> getAll(){
        return genreDao.getAll();
    }

    @Override
    public Author save(Author author){
        return genreDao.save(author);
    }

    @Override
    public Author updateById(Long id, Author author){
        genreDao.updateById(id, author);
        return genreDao.getById(id);
    }

    @Override
    public void deleteById(Long id){
        genreDao.deleteById(id);
    }
}
