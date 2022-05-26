package ru.otus.spring.task.manager.web.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.task.manager.service.TaskService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;

@RestController
@RequestMapping(TaskAdminController.URL)
@RequiredArgsConstructor
public class TaskAdminController {

    public static final String URL = "/api/1/admin/task";

    private final TaskService taskService;
    private final ObjectMapperUtils objectMapperUtils;

    @GetMapping("{key}")
    public TaskResponseDto getTaskByKeyWithFullFields(@PathVariable String key) {
        return objectMapperUtils.map(taskService.getTaskByKeySuperMode(key), TaskResponseDto.class);
    }

    @PostMapping(value = "{key}/status/id/{statusId}")
    public TaskResponseDto setTaskStatus(@PathVariable String key, @PathVariable Long statusId) {
        return objectMapperUtils.map(taskService.setStatusSuperMode(key, statusId), TaskResponseDto.class);
    }

    @PostMapping(value = "{key}/status/name/{statusName}")
    public TaskResponseDto setTaskStatus(@PathVariable String key, @PathVariable String statusName) {
        return objectMapperUtils.map(taskService.setStatusSuperMode(key, statusName), TaskResponseDto.class);
    }
}
