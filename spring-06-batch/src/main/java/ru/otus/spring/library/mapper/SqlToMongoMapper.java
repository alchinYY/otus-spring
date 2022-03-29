package ru.otus.spring.library.mapper;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.mongo.BookMng;
import ru.otus.spring.library.domain.sql.Book;

@Component
public class SqlToMongoMapper extends PropertyMap<Book, BookMng> {

    @Override
    protected void configure() {
        Converter<Book, BookMng> converterGenre = new AbstractConverter<>() {
            @Override
            protected BookMng convert(Book source) {
                return new BookMng(source.getId().toString(), source.getName());
            }
        };

        using(converterGenre).map(source.getGenre(), destination.getGenre());
    }

}
