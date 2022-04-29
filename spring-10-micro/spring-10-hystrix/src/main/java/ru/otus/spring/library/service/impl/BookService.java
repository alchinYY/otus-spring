package ru.otus.spring.library.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.config.DefaultBookConfig;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@Service
public class BookService implements EntityService<Book> {

    private final Book defaultBook;
    private final JpaRepository<Book, Long> bookRepository;

    public BookService(
            DefaultBookConfig defaultBookConfig,
            JpaRepository<Book, Long> bookRepository
    ) {
        defaultBook = defaultBookConfig.getDefaultBook();
        this.bookRepository = bookRepository;
    }

    @Override
    @HystrixCommand(commandKey = "getBookKey", fallbackMethod = "getByIdIfNotFound")
    public Book getById(long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("book"));
    }


    public Book getByIdIfNotFound(long id) {
        return defaultBook;
    }

    @Override
    @HystrixCommand(commandKey = "getAllKey")
            public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    @HystrixCommand(commandKey = "saveBookKey")
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    @HystrixCommand(commandKey = "updateBookKey")
    public Book updateById(Long bookId, Book book) {
        Book fromDb = getById(bookId);
        fromDb.setName(book.getName());
        fromDb.setGenre(book.getGenre());
        fromDb.setAuthors(book.getAuthors());
        return fromDb;
    }

    @Override
    @HystrixCommand(commandKey = "deleteBookKey")
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
