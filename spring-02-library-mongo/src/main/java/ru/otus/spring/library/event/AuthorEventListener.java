package ru.otus.spring.library.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Author;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.exception.DomainExistException;
import ru.otus.spring.library.repo.BookRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorEventListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        List<Book> books = bookRepository.findByAuthorsId(event.getSource().getInteger("_id"));
        if(books.isEmpty()){
            super.onBeforeDelete(event);
        } else {
            throw new DomainExistException("books", books);
        }
    }
}
