package ru.otus.spring.task.manager.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import ru.otus.spring.task.manager.web.dto.resp.TaskMinimizedDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

import static ru.otus.spring.task.manager.web.ControllerMessageConstant.*;

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
    @Operation(summary = "Создание задачи", responses = {
            @ApiResponse(responseCode = "201", description = MSG_CREATED),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    public TaskResponseDto createTask(
            @Valid @RequestBody CreatingTaskDto taskDto
    ) {
        TaskEntity taskEntity = objectMapperUtils.map(taskDto, TaskEntity.class);
        taskEntity.setCreator(securityUtil.getCurrentUser());
        return objectMapperUtils.map(taskService.createTask(taskEntity, taskDto.getProjectKey()), TaskResponseDto.class);
    }

    @Operation(summary = "Получение задачи по ключу", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("{key}")
    public TaskResponseDto getTaskByKey(
            @Parameter(description = "Ключ задачи. <Ключ проекта>-<номер задачи>", example = "KEY-1")
            @PathVariable String key
    ) {
        return objectMapperUtils.map(taskService.getTaskByKey(key), TaskResponseDto.class);
    }

    @Operation(summary = "Прикрепление к задаче вложений", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(value = "{key}/attachment", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public TaskResponseDto addTaskAttach(
            @Parameter(description = "Ключ задачи. <Ключ проекта>-<номер задачи>", example = "KEY-1")
            @PathVariable String key,
            @Parameter(description = "Файл со вложением") @RequestPart MultipartFile file
    ) {
        return objectMapperUtils.map(taskService.addAttach(key, securityUtil.getCurrentUser(), file), TaskResponseDto.class);
    }

    @Operation(summary = "Установление в задаче статуса по id", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(value = "{key}/status/id/{statusId}")
    public TaskResponseDto setTaskStatus(
            @Parameter(description = "Ключ задачи. <Ключ проекта>-<номер задачи>", example = "KEY-1")
            @PathVariable String key,
            @Parameter(description = "id статуса, который нужно установить", example = "2")
            @PathVariable Long statusId
    ) {
        return objectMapperUtils.map(taskService.setStatus(key, statusId), TaskResponseDto.class);
    }

    @Operation(summary = "Установка исполнителя по login", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })

    @PostMapping(value = "{key}/assignee/{login}")
    public TaskResponseDto setAssignee(
            @Parameter(description = "Ключ задачи. <Ключ проекта>-<номер задачи>", example = "KEY-1")
            @PathVariable String key,
            @Parameter(description = "login нового assignee", example = "user")
            @PathVariable String login
    ) {
        return objectMapperUtils.map(taskService.setAssignee(key, login), TaskResponseDto.class);
    }

    @Operation(summary = "Установление в задаче статуса по имени статуса", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping(value = "{key}/status/name/{statusName}")
    public TaskResponseDto setTaskStatus(
            @Parameter(description = "Ключ задачи. <Ключ проекта>-<номер задачи>", example = "KEY-1")
            @PathVariable String key,
            @Parameter(description = "Имя статуса", example = "todo")
            @PathVariable String statusName
    ) {
        return objectMapperUtils.map(taskService.setStatus(key, statusName), TaskResponseDto.class);
    }


    @Operation(summary = "Получение задач, в которые на мне", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("filter/on-me")
    public List<TaskMinimizedDto> getTaskOnMe() {
        return objectMapperUtils.mapAll(taskService.getTasksByAssignee(securityUtil.getCurrentUser()), TaskMinimizedDto.class);
    }


    @Operation(summary = "Получение задач по ключу проекта", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("filter/project/{key}")
    public List<TaskMinimizedDto> getTaskOnProject(
            @Parameter(description = "Ключ проекта", example = "KEY")
            @Pattern(regexp = "[A-Z0-9]+")
            @PathVariable String key
    ) {
        return objectMapperUtils.mapAll(taskService.getTasksByProject(key), TaskMinimizedDto.class);
    }

}
