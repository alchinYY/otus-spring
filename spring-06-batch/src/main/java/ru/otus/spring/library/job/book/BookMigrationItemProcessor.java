package ru.otus.spring.library.job.book;

import org.springframework.batch.item.ItemProcessor;
import ru.otus.spring.library.domain.mongo.BookMng;
import ru.otus.spring.library.domain.sql.Book;

import java.util.stream.Collectors;

public class BookMigrationItemProcessor implements ItemProcessor<Book, BookMng> {

    @Override
    public BookMng process(Book book) {
        BookMng bookMng = new BookMng(book.getId().toString(), book.getName());
        bookMng.setAuthors(book.getAuthors().stream().map(a -> a.getId().toString()).collect(Collectors.toList()));
        bookMng.setGenre(book.getGenre().getId().toString());
        return bookMng;
    }
}