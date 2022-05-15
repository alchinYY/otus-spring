package ru.otus.spring.task.manager.service;

import ru.otus.spring.task.manager.model.TaskCommentEntity;

public interface CommentService {

    void createComment(String taskKey, TaskCommentEntity entity);

}
