package ru.otus.spring.task.manager.web;

import lombok.RequiredArgsConstructor;
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
public class TaskController {

    private final TaskService taskService;
    private final ObjectMapperUtils objectMapperUtils;
    private final SecurityUtil securityUtil;

    @PostMapping
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
