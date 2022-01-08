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

    private final Dao<Long, Author> authorDao;

    @Override
    public Author getById(long id){
        return authorDao.getById(id)
                .orElseThrow(() -> new DomainNotFound("author"));
    }

    @Override
    public List<Author> getAll(){
        return authorDao.getAll();
    }

    @Override
    @Transactional
    public Author save(Author author){
        return authorDao.save(author);
    }

    @Override
    @Transactional
    public Author updateById(Long id, Author author){
        Author authorFromDb = getById(id);
        authorFromDb.setName(author.getName());
        return authorFromDb;
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        authorDao.deleteById(id);
    }
}
