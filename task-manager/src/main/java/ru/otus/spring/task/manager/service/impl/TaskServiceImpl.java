package ru.otus.spring.task.manager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.task.manager.exception.StatusNotAvailableForTransitionException;
import ru.otus.spring.task.manager.exception.TaskNotFoundException;
import ru.otus.spring.task.manager.model.ProjectEntity;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.TaskStatusEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.model.flow.TaskStatusNodeEntity;
import ru.otus.spring.task.manager.repo.TaskRepo;
import ru.otus.spring.task.manager.repo.TaskStatusNodeRepo;
import ru.otus.spring.task.manager.service.AttachFileService;
import ru.otus.spring.task.manager.service.ProjectService;
import ru.otus.spring.task.manager.service.TaskService;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final String TASK_NAME_SEPARATOR = "-";

    private final TaskRepo taskRepo;
    private final AttachFileService attachFileService;
    private final ProjectService projectService;
    private final TaskStatusNodeRepo taskStatusNodeRepo;
    private final UserService userService;

    @Override
    @Transactional
    public TaskEntity createTask(TaskEntity task, String projectKey) {
        ProjectEntity projectEntity = projectService.getByKey(projectKey);
        projectEntity.setTaskCounter(projectEntity.getTaskCounter() + 1);
        task.setKey(projectKey + TASK_NAME_SEPARATOR + projectEntity.getTaskCounter());
        task.setTaskStatus(projectEntity.getWorkflow().getStartStatus());
        projectEntity.getTasks().add(task);
        return task;
    }

    @Override
    public TaskEntity getTaskByKeySuperMode(String taskKey) {
        return taskRepo.findAllEntityByKey(taskKey)
                .orElseThrow(() -> new TaskNotFoundException("Task with key \"" + taskKey + "\" not found"));
    }

    @Override
    public TaskEntity getTaskByKey(String taskKey) {
        return taskRepo.findByKey(taskKey)
                .orElseThrow(() -> new TaskNotFoundException("Task with key \"" + taskKey + "\" not found"));
    }

    @Override
    @Transactional
    public TaskEntity addAttach(String taskKey, UserEntity userEntity, MultipartFile file) {
        TaskEntity taskEntity = getTaskByKeySuperMode(taskKey);
        taskEntity
                .getAttachments()
                .add(attachFileService.uploadFile(taskKey, userEntity, file));
        return taskEntity;
    }

    @Override
    @Transactional
    public TaskEntity setStatus(String taskKey, String status) {
        return setStatus(taskKey, e -> e.getName().equals(status));
    }

    @Override
    @Transactional
    public TaskEntity setStatus(String taskKey, long statusId) {
        return setStatus(taskKey, e -> e.getId().equals(statusId));
    }

    @Override
    @Transactional
    public TaskEntity setStatusSuperMode(String taskKey, String status) {
        return setStatusSuperMode(taskKey, sn -> sn.getNode().getName().equals(status));
    }

    @Override
    @Transactional
    public TaskEntity setStatusSuperMode(String taskKey, long statusId) {
        return setStatusSuperMode(taskKey, sn -> sn.getNode().getId().equals(statusId));
    }

    private TaskEntity setStatus(String taskKey, Predicate<TaskStatusEntity> predicate) {
        TaskEntity taskEntity = getTaskByKeySuperMode(taskKey);
        TaskStatusEntity currentStatus = taskEntity.getTaskStatus();
        ProjectEntity projectEntity = projectService.getByKey(getProjectKey(taskEntity));
        TaskStatusEntity statusEntity = taskStatusNodeRepo.getByNodeTaskStatusIdAndProjectId(currentStatus.getId(), projectEntity.getId())
                .orElseThrow(() -> new StatusNotAvailableForTransitionException("Task status does not contain transitions"))
                .getEdges().stream()
                .filter(predicate)
                .findAny()
                .orElseThrow(() -> new StatusNotAvailableForTransitionException("Status not available for transition"));

        taskEntity.setTaskStatus(statusEntity);
        return taskEntity;
    }

    private TaskEntity setStatusSuperMode(String taskKey, Predicate<TaskStatusNodeEntity> predicate) {
        TaskEntity taskEntity = getTaskByKeySuperMode(taskKey);
        ProjectEntity projectEntity = projectService.getByKey(getProjectKey(taskEntity));

        TaskStatusEntity newStatus = projectEntity.getWorkflow().getStatusNodes().stream().filter(predicate)
                .findAny()
                .orElseThrow(() -> new StatusNotAvailableForTransitionException("Status not available for transition"))
                .getNode();
        taskEntity.setTaskStatus(newStatus);
        return taskEntity;
    }

    @Override
    public String getProjectKey(TaskEntity taskEntity) {
        return taskEntity.getKey().split(TASK_NAME_SEPARATOR)[0];
    }

    @Override
    public List<TaskEntity> getTasksByAssignee(UserEntity userEntity) {
        return taskRepo.findByAssignee(userEntity);
    }

    @Override
    public Set<TaskEntity> getTasksByProject(String projectKey) {
        return projectService.getByKey(projectKey).getTasks();
    }

    @Override
    @Transactional
    public TaskEntity setAssignee(String taskKey, String login) {
        TaskEntity taskEntity = getTaskByKeySuperMode(taskKey);
        taskEntity.setAssignee(userService.loadUserByUsername(login));
        return taskEntity;
    }
}
