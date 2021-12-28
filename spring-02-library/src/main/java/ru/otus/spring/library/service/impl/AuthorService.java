package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorService implements EntityService<Author> {

    private final Dao<Long, Author> genreDao;

    @Override
    @Transactional(readOnly = true)
    public Author getById(long id){
        return genreDao.getById(id)
                .orElseThrow(() -> new DomainNotFound("author"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Author> getAll(){
        return genreDao.getAll();
    }

    @Override
    @Transactional
    public Long save(Author author){
        return genreDao.save(author).getId();
    }

    @Override
    @Transactional
    public Author updateById(Long id, Author author){
        genreDao.updateById(id, author);
        return getById(id);
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        genreDao.deleteById(id);
    }
}
