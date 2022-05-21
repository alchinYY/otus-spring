package ru.otus.spring.task.manager.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.service.TaskService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.CreatingTaskDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1/task")
@Tag(name = "task-controller", description = "Управление задачами")
public class TaskController {

    private final TaskService taskService;
    private final ObjectMapperUtils objectMapperUtils;
    private final SecurityUtil securityUtil;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    @Operation(summary = "Получить пользователя по id", responses = {
            @ApiResponse(responseCode = "201", description = ControllerMessageConstant.MSG_OK),
            @ApiResponse(responseCode = "400", description = ControllerMessageConstant.MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = ControllerMessageConstant.MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = ControllerMessageConstant.MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = ControllerMessageConstant.MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = ControllerMessageConstant.MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    public TaskResponseDto createTask(@RequestBody CreatingTaskDto taskDto) {
        TaskEntity taskEntity = objectMapperUtils.map(taskDto, TaskEntity.class);
        taskEntity.setCreator(securityUtil.getCurrentUser());
        return objectMapperUtils.map(taskService.createTask(taskEntity, taskDto.getProjectKey()), TaskResponseDto.class);
    }

    @GetMapping("{key}")
    public TaskResponseDto getTaskByKey(@PathVariable String key) {
        return objectMapperUtils.map(taskService.getTaskByKey(key), TaskResponseDto.class);
    }

    @PostMapping(value = "{key}/attachment", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public TaskResponseDto addTaskAttach(@PathVariable String key, @RequestPart MultipartFile file) {
        return objectMapperUtils.map(taskService.addAttach(key, securityUtil.getCurrentUser(), file), TaskResponseDto.class);
    }

    @PostMapping(value = "{key}/status/id/{statusId}")
    public TaskResponseDto setTaskStatus(@PathVariable String key, @PathVariable Long statusId) {
        return objectMapperUtils.map(taskService.setStatus(key, statusId), TaskResponseDto.class);
    }

    @PostMapping(value = "{key}/status/name/{statusName}")
    public TaskResponseDto setTaskStatus(@PathVariable String key, @PathVariable String statusName) {
        return objectMapperUtils.map(taskService.setStatus(key, statusName), TaskResponseDto.class);
    }
}
