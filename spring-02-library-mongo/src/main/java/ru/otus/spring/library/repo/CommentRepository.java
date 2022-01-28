package ru.otus.spring.library.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.spring.library.domain.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
}
