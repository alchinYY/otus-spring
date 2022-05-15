package ru.otus.spring.task.manager.web;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.service.CommentService;
import ru.otus.spring.task.manager.web.dto.CreatingTaskCommentDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/1/comment")
public class CommentController {

    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final SecurityUtil securityUtil;

    @PostMapping
    public void createComment(@RequestBody CreatingTaskCommentDto dto) {
        TaskCommentEntity taskCommentEntity = modelMapper.map(dto, TaskCommentEntity.class);
        taskCommentEntity.setAuthor(securityUtil.getCurrentUser());
        commentService.createComment(dto.getTaskKey(), taskCommentEntity);
    }

}
