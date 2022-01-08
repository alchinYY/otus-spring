package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService implements EntityService<Book> {

    private final JpaRepository<Book, Long> bookRepository;

    @Override
    public Book getById(long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("book"));
    }

    @Override
    public List<Book> getAll(){
        return bookRepository.findAll();
    }

    @Override
    @Transactional
    public Book save(Book book){
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateById(Long bookId, Book book){
        book.setId(bookId);
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        bookRepository.deleteById(id);
    }

}
