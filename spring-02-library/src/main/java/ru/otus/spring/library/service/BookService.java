package ru.otus.spring.library.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final Dao<Long, Book> bookDao;
    private final Dao<Long, Genre> genreDao;
    private final Dao<Long, Author> authorDao;

    public Book getById(Long id){
        return bookDao.getById(id);
    }

    public List<Book> getAll(){
        return bookDao.getAll();
    }

    public Book create(String name, Long genreId, String authorsIdList){
        Book book = createBookBody(name, genreId, authorsIdList);
        return bookDao.save(book);
    }

    @Transactional
    public Book updateById(Long bookId, String name, Long genreId, String authorsIdList){
        bookDao.updateById(bookId, createBookBody(name, genreId, authorsIdList));
        return bookDao.getById(bookId);
    }

    public void deleteById(Long id){
        bookDao.deleteById(id);
    }

    private Book createBookBody(String name, Long genreId, String authorsIdList) {
        Genre genre = genreDao.getById(genreId);

        List<Author> authors = Arrays.stream(authorsIdList.split(";"))
                .map(Long::parseLong)
                .map(authorDao::getById)
                .collect(Collectors.toList());

        return Book.builder()
                .name(name)
                .authors(authors)
                .genre(genre)
                .build();
    }
}
