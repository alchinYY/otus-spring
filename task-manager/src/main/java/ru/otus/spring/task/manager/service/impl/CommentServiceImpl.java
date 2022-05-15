package ru.otus.spring.task.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.service.CommentService;
import ru.otus.spring.task.manager.service.TaskService;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final TaskService taskService;

    @Override
    @Transactional
    public void createComment(String taskKey, TaskCommentEntity entity) {
        taskService.getTaskByKey(taskKey).getComments().add(entity);
    }
}
