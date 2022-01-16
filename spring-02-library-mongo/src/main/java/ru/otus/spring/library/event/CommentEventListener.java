package ru.otus.spring.library.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.repo.BookRepository;

@Component
@RequiredArgsConstructor
public class CommentEventListener extends AbstractMongoEventListener<Comment> {

    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Comment> event) {
        int id = event.getSource().getInteger("_id");

        bookRepository.findByCommentsId(id)
                .ifPresent(book -> {
                    book.getComments().removeIf(comment -> comment.getId().equals(id));
                    bookRepository.save(book);
                });

        super.onBeforeDelete(event);
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Comment> event) {
        Book book = event.getSource().getBook();
        book.getComments().add(event.getSource());
        bookRepository.save(book);
        super.onAfterSave(event);
    }
}
