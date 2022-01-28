package ru.otus.spring.library.service.impl;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.exception.DomainNotFound;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CrudRepository<Comment, Integer> commentRepository;
    private final BookService bookService;
    private final SequenceGeneratorService<Integer> sequenceGeneratorService;

    public Comment getById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new DomainNotFound("comment"));
    }

    public List<Comment> getAll() {
        return Lists.newArrayList(commentRepository.findAll());
    }

    public List<Comment> getAllByBookId(Integer id) {
        return bookService.getById(id).getComments();
    }

    @Transactional
    public Comment save(Comment comment, int bookId) {
        comment.setId(sequenceGeneratorService.generateSequence(Comment.SEQUENCE_NAME));
        comment.setBook(bookService.getById(bookId));
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateById(int id, Comment comment) {
        Comment commentFromDb = getById(id);
        commentFromDb.setBody(comment.getBody());
        return commentRepository.save(commentFromDb);
    }

    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }
}
