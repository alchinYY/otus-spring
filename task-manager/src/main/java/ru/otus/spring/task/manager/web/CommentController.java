package ru.otus.spring.task.manager.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.service.CommentService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.CreatingTaskCommentDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(CommentController.URL)
public class CommentController {

    public static final String URL = "/api/1/comment";

    private final CommentService commentService;
    private final ObjectMapperUtils objectMapperUtils;
    private final SecurityUtil securityUtil;

    @PostMapping
    public void createComment(@RequestBody CreatingTaskCommentDto dto) {
        TaskCommentEntity taskCommentEntity = objectMapperUtils.map(dto, TaskCommentEntity.class);
        taskCommentEntity.setAuthor(securityUtil.getCurrentUser());
        commentService.createComment(dto.getTaskKey(), taskCommentEntity);
    }

}
