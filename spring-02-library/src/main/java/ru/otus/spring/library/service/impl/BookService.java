package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.service.EntityService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements EntityService<Book> {

    private final Dao<Long, Book> bookDao;

    @Override
    public Book getById(long id){
        return bookDao.getById(id);
    }

    @Override
    public List<Book> getAll(){
        return bookDao.getAll();
    }

    @Override
    public Long save(Book book){
        return bookDao.save(book);
    }

    @Transactional
    @Override
    public Book updateById(Long bookId, Book book){
        bookDao.updateById(bookId, book);
        return bookDao.getById(bookId);
    }

    @Override
    public void deleteById(Long id){
        bookDao.deleteById(id);
    }

}
