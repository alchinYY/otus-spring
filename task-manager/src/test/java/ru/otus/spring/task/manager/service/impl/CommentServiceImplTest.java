package ru.otus.spring.task.manager.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.service.CommentService;
import ru.otus.spring.task.manager.service.TaskService;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
@Import(CommentServiceImpl.class)
class CommentServiceImplTest {

    private static final String TASK_KEY = "TASK-1";

    @Autowired
    private CommentService commentService;

    @MockBean
    private TaskService taskService;

    @Test
    void createComment() {

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setComments(new HashSet<>());

        when(taskService.getTaskByKey(TASK_KEY)).thenReturn(taskEntity);
        assertThat(taskEntity.getComments()).isEmpty();

        TaskCommentEntity taskCommentEntity = new TaskCommentEntity();
        taskCommentEntity.setBody("body");
        commentService.createComment(TASK_KEY, taskCommentEntity);

        assertThat(taskEntity.getComments())
                .isNotEmpty()
                .contains(taskCommentEntity);
    }
}
