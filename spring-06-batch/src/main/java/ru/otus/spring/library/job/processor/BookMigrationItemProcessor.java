package ru.otus.spring.library.job.processor;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.spring.library.domain.mongo.BookMng;
import ru.otus.spring.library.domain.sql.Book;

@RequiredArgsConstructor
public class BookMigrationItemProcessor implements ItemProcessor<Book, BookMng> {

    private final ModelMapper modelMapper;

    @Override
    public BookMng process(Book book) throws Exception {
        System.out.println(book);
        System.out.println(modelMapper.map(book, BookMng.class));
        return modelMapper.map(book, BookMng.class);
    }
}
