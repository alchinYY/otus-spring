package ru.otus.spring.library.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;

@Component
@RequiredArgsConstructor
public class BookEventListener extends AbstractMongoEventListener<Book> {

    private final CrudRepository<Book, Integer> bookRepository;
    private final CrudRepository<Comment, Integer> commentRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {

        bookRepository.findById(event.getSource().getInteger("_id")).ifPresent(
                book -> {
                    commentRepository.deleteAll(book.getComments());
                    super.onBeforeDelete(event);
                }
        );

    }
}
