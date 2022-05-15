package ru.otus.spring.task.manager.service;

import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.UserEntity;

public interface TaskService {

    TaskEntity createTask(TaskEntity task, String projectKey);

    TaskEntity getTaskByKeySuperMode(String taskKey);

    TaskEntity getTaskByKey(String taskKey);

    TaskEntity addAttach(String taskKey, UserEntity owner, MultipartFile file);

    TaskEntity setStatus(String taskKey, String status);

    TaskEntity setStatus(String taskKey, long statusId);

    TaskEntity setStatusSuperMode(String taskKey, String status);

    TaskEntity setStatusSuperMode(String taskKey, long statusId);

    String getProjectKey(TaskEntity taskEntity);
}
