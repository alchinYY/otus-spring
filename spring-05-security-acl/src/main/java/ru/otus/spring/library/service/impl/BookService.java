package ru.otus.spring.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.acls.domain.BasePermission;
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
    private final CustomAclService customAclService;

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
    public Book save(Book book){
        Book savedBook = bookRepository.save(book);
        customAclService.createAcl(savedBook.getId(), Book.class, BasePermission.READ, BasePermission.WRITE);
        return savedBook;
    }

    @Override
    @Transactional
    public Book updateById(Long bookId, Book book){
        Book fromDb = getById(bookId);
        fromDb.setName(book.getName());
        fromDb.setGenre(book.getGenre());
        fromDb.setAuthors(book.getAuthors());
        return fromDb;
    }

    @Override
    @Transactional
    public void deleteById(Long id){
        bookRepository.delete(getById(id));
    }

}
