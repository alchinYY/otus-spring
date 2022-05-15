package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.TaskCommentEntity;

@Repository
public interface TaskCommentRepo extends JpaRepository<TaskCommentEntity, Long> {
}
