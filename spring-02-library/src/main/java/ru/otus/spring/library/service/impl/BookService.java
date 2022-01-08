package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements EntityService<Book> {

    private final Dao<Long, Book> bookDao;

    @Override
    public Book getById(long id){
        return bookDao.getById(id)
                .orElseThrow(() -> new DomainNotFound("book"));
    }

    @Override
    public List<Book> getAll(){
        return bookDao.getAll();
    }

    @Override
    @Transactional
    public Book save(Book book){
        return bookDao.save(book);
    }

    @Override
    @Transactional
    public Book updateById(Long bookId, Book book){
        book.setId(bookId);
        return bookDao.save(book);
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        bookDao.deleteById(id);
    }

}
