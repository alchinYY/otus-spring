package ru.otus.spring.library.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.exception.DomainExistException;
import ru.otus.spring.library.repo.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class GenreEventListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;
    private final MongoOperations mongoOperations;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        List<Book> books = bookRepository.findByGenreId(event.getSource().getInteger("_id"));
         if(books.isEmpty()){
             super.onBeforeDelete(event);
         } else {
             throw new DomainExistException("books", books);
         }
    }
}
