package ru.otus.spring.task.manager.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.service.CommentService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.CreatingTaskCommentDto;

import javax.validation.Valid;

import static ru.otus.spring.task.manager.web.ControllerMessageConstant.*;
import static ru.otus.spring.task.manager.web.ControllerMessageConstant.MSG_INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping(CommentController.URL)
@Tag(name = "comment-controller", description = "Api для управления комментариями")
public class CommentController {

    public static final String URL = "/api/1/comment";

    private final CommentService commentService;
    private final ObjectMapperUtils objectMapperUtils;
    private final SecurityUtil securityUtil;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Создание комметария", responses = {
            @ApiResponse(responseCode = "201", description = MSG_CREATED),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    public void createComment(@Valid @RequestBody CreatingTaskCommentDto dto) {
        TaskCommentEntity taskCommentEntity = objectMapperUtils.map(dto, TaskCommentEntity.class);
        taskCommentEntity.setAuthor(securityUtil.getCurrentUser());
        commentService.createComment(dto.getTaskKey(), taskCommentEntity);
    }

}
