package ru.otus.spring.library.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.EntityService;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService implements EntityService<Book> {

    private final CrudRepository<Book, Integer> bookRepository;
    private final SequenceGeneratorService<Integer> sequenceGeneratorService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @Override
    public Book getById(int id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("book"));
    }

    @Override
    public List<Book> getAll(){
        return Lists.newArrayList(bookRepository.findAll());
    }

    @Override
    public Book save(Book book){
        book.setId(sequenceGeneratorService.generateSequence(Book.SEQUENCE_NAME));

        book.setGenre(genreService.getById(book.getGenre().getId()));
        book.setAuthors(
                book.getAuthors().stream()
                        .map(a -> authorService.getById(a.getId()))
                        .collect(Collectors.toList())
        );

        return bookRepository.save(book);
    }

    @Override
    public Book updateById(int bookId, Book book){
        Book fromDb = getById(bookId);
        fromDb.setName(book.getName());
        fromDb.setGenre(book.getGenre());
        fromDb.setAuthors(book.getAuthors());
        return bookRepository.save(fromDb);
    }

    @Override
    public void deleteById(int id){
        bookRepository.deleteById(id);
    }

}
