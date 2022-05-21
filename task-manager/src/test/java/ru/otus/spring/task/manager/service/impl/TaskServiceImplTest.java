package ru.otus.spring.task.manager.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import ru.otus.spring.task.manager.exception.StatusNotAvailableForTransitionException;
import ru.otus.spring.task.manager.exception.TaskNotFoundException;
import ru.otus.spring.task.manager.model.*;
import ru.otus.spring.task.manager.model.enumerates.StatusEntity;
import ru.otus.spring.task.manager.model.flow.TaskStatusNodeEntity;
import ru.otus.spring.task.manager.model.flow.WorkflowEntity;
import ru.otus.spring.task.manager.repo.TaskRepo;
import ru.otus.spring.task.manager.repo.TaskStatusNodeRepo;
import ru.otus.spring.task.manager.service.AttachFileService;
import ru.otus.spring.task.manager.service.ProjectService;
import ru.otus.spring.task.manager.service.TaskService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TaskServiceImpl.class)
class TaskServiceImplTest {

    private static final String TASK_PROJECT_KEY = "KEY";
    private static final String TASK_KEY = TASK_PROJECT_KEY + "-1";
    private static final TaskStatusEntity TODO_STATUS = new TaskStatusEntity(1L, "todo");
    private static final TaskStatusEntity IN_PROGRESS_STATUS = new TaskStatusEntity(2L, "in_progress");
    private static final TaskStatusEntity SECRET_STATUS = new TaskStatusEntity(100L, "secret");
    private static final String ASSIGNEE_LOGIN = "login";

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepo taskRepo;
    @MockBean
    private AttachFileService attachFileService;
    @MockBean
    private ProjectService projectService;
    @MockBean
    private TaskStatusNodeRepo taskStatusNodeRepo;

    @Test
    void createTask() {
        TaskEntity beforeTaskEntity = createTaskEntity();
        beforeTaskEntity.setKey(null);
        beforeTaskEntity.setId(null);
        ProjectEntity projectEntity = createProjectEntity();
        when(projectService.getByKey(TASK_PROJECT_KEY)).thenReturn(projectEntity);

        TaskEntity taskEntityAfter = createTaskEntity();
        assertThat(taskService.createTask(beforeTaskEntity, TASK_PROJECT_KEY))
                .extracting(TaskEntity::getKey, TaskEntity::getTaskStatus)
                .contains(taskEntityAfter.getKey(), projectEntity.getWorkflow().getStartStatus());

        verify(projectService, times(1)).getByKey(any());

    }

    @Test
    void getTaskByKeySuperMode() {
        TaskEntity taskEntity = createTaskEntity();
        when(taskRepo.findAllEntityByKey(taskEntity.getKey())).thenReturn(Optional.of(taskEntity));

        assertThat(taskService.getTaskByKeySuperMode(taskEntity.getKey())).isEqualTo(taskEntity);
        verify(taskRepo, times(1)).findAllEntityByKey(any());
    }

    @Test
    void getTaskByKey() {
        TaskEntity taskEntity = createTaskEntity();
        when(taskRepo.findByKey(taskEntity.getKey())).thenReturn(Optional.of(taskEntity));

        assertThat(taskService.getTaskByKey(taskEntity.getKey())).isEqualTo(taskEntity);
        verify(taskRepo, times(1)).findByKey(any());
    }

    @Test
    void getTaskByKey_notFound() {
        when(taskRepo.findByKey(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskByKey(TASK_KEY))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("not found");
        verify(taskRepo, times(1)).findByKey(any());
    }

    @Test
    void addAttach() {
        TaskEntity taskEntity = createTaskEntity();
        when(taskRepo.findAllEntityByKey(taskEntity.getKey())).thenReturn(Optional.of(taskEntity));
        when(attachFileService.uploadFile(any(), any(), any())).thenReturn(new AttachmentEntity());
        int currentSize = taskEntity.getAttachments().size();

        TaskEntity updatedTaskEntity = taskService.addAttach(taskEntity.getKey(), new UserEntity(), mock(MockMultipartFile.class));
        assertThat(updatedTaskEntity.getAttachments()).hasSize(currentSize + 1);
    }

    @Test
    void setStatusId() {
        TaskEntity taskEntity = createTaskEntity();
        TaskStatusNodeEntity taskStatusNodeEntity = createTaskStatusNode(TODO_STATUS, List.of(IN_PROGRESS_STATUS));
        createMockForStatusTest(taskEntity, taskStatusNodeEntity);
        assertThat(taskService.setStatus(taskEntity.getKey(), IN_PROGRESS_STATUS.getId()))
                .extracting(TaskEntity::getKey, TaskEntity::getTaskStatus)
                .contains(taskEntity.getKey(), IN_PROGRESS_STATUS);
    }

    @Test
    void setStatusId_statusNotAvailable() {
        TaskEntity taskEntity = createTaskEntity();
        TaskStatusNodeEntity taskStatusNodeEntity = createTaskStatusNode(TODO_STATUS, null);
        createMockForStatusTest(taskEntity, taskStatusNodeEntity);
        assertThatExceptionOfType(StatusNotAvailableForTransitionException.class)
                .isThrownBy(() -> taskService.setStatus(taskEntity.getKey(), 2));
    }

    @Test
    void setStatusKey() {
        TaskEntity taskEntity = createTaskEntity();
        TaskStatusNodeEntity taskStatusNodeEntity = createTaskStatusNode(TODO_STATUS, List.of(IN_PROGRESS_STATUS));

        createMockForStatusTest(taskEntity, taskStatusNodeEntity);

        assertThat(taskService.setStatus(taskEntity.getKey(), IN_PROGRESS_STATUS.getName()))
                .extracting(TaskEntity::getKey, TaskEntity::getTaskStatus)
                .contains(taskEntity.getKey(), IN_PROGRESS_STATUS);
    }

    @Test
    void setStatusSuperModeId() {
        TaskEntity taskEntity = createTaskEntity();
        TaskStatusNodeEntity taskStatusNodeEntity = createTaskStatusNode(TODO_STATUS, null);

        createMockForStatusTest(taskEntity, taskStatusNodeEntity);

        assertThat(taskService.setStatusSuperMode(taskEntity.getKey(), IN_PROGRESS_STATUS.getId()))
                .extracting(TaskEntity::getKey, TaskEntity::getTaskStatus)
                .contains(taskEntity.getKey(), IN_PROGRESS_STATUS);
    }

    @Test
    void setStatusSuperModeId_statusNotFoundInWorkflow() {
        TaskEntity taskEntity = createTaskEntity();
        TaskStatusNodeEntity taskStatusNodeEntity = createTaskStatusNode(TODO_STATUS, List.of(IN_PROGRESS_STATUS));
        createMockForStatusTest(taskEntity, taskStatusNodeEntity);
        assertThatExceptionOfType(StatusNotAvailableForTransitionException.class)
                .isThrownBy(() -> taskService.setStatusSuperMode(taskEntity.getKey(), SECRET_STATUS.getId()));

    }

    @Test
    void setStatusSuperModeKey() {
        TaskEntity taskEntity = createTaskEntity();
        TaskStatusNodeEntity taskStatusNodeEntity = createTaskStatusNode(TODO_STATUS, null);

        createMockForStatusTest(taskEntity, taskStatusNodeEntity);

        assertThat(taskService.setStatusSuperMode(taskEntity.getKey(), IN_PROGRESS_STATUS.getName()))
                .extracting(TaskEntity::getKey, TaskEntity::getTaskStatus)
                .contains(taskEntity.getKey(), IN_PROGRESS_STATUS);
    }

    @Test
    void getProjectKey() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setKey(TASK_KEY);

        assertThat(taskService.getProjectKey(taskEntity))
                .isEqualTo(TASK_PROJECT_KEY);
    }

    @Test
    void getByAssignee() {
        TaskEntity taskEntity = createTaskEntity();

        when(taskRepo.findByAssignee(any())).thenReturn(List.of(taskEntity));

        assertThat(taskService.getTasksByAssignee(taskEntity.getAssignee()))
                .isNotEmpty()
                .containsOnly(taskEntity);

        verify(taskRepo, times(1)).findByAssignee(any());
    }

    private TaskStatusNodeEntity createTaskStatusNode(TaskStatusEntity node, List<TaskStatusEntity> edges) {
        TaskStatusNodeEntity taskStatusNodeEntity = new TaskStatusNodeEntity();
        taskStatusNodeEntity.setNode(node);
        Optional.ofNullable(edges).ifPresent(e -> taskStatusNodeEntity.getEdges().addAll(e));
        return taskStatusNodeEntity;
    }

    private ProjectEntity createProjectEntity() {
        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setKey(TASK_PROJECT_KEY);
        WorkflowEntity workflowEntity = new WorkflowEntity();
        workflowEntity.setStartStatus(TODO_STATUS);
        workflowEntity.getStatusNodes().add(createTaskStatusNode(TODO_STATUS, null));
        workflowEntity.getStatusNodes().add(createTaskStatusNode(IN_PROGRESS_STATUS, null));

        projectEntity.setWorkflow(workflowEntity);
        return projectEntity;
    }

    private static TaskEntity createTaskEntity() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setKey(TASK_KEY);
        taskEntity.setTaskStatus(TODO_STATUS);
        taskEntity.setAssignee(new UserEntity(ASSIGNEE_LOGIN));
        AttachmentEntity attachmentEntityDeleted = new AttachmentEntity();
        attachmentEntityDeleted.setStatus(StatusEntity.DELETED);
        attachmentEntityDeleted.setId(1L);

        AttachmentEntity attachmentEntityActive = new AttachmentEntity();
        attachmentEntityActive.setId(2L);

        taskEntity.getAttachments().add(attachmentEntityDeleted);
        taskEntity.getAttachments().add(attachmentEntityActive);

        return taskEntity;
    }

    private void createMockForStatusTest(TaskEntity taskEntity, TaskStatusNodeEntity taskStatusNodeEntity) {
        when(taskRepo.findAllEntityByKey(taskEntity.getKey())).thenReturn(Optional.of(taskEntity));
        when(taskStatusNodeRepo.getByNodeTaskStatusIdAndProjectId(any(), any())).thenReturn(Optional.of(taskStatusNodeEntity));
        when(projectService.getByKey(any())).thenReturn(createProjectEntity());
    }
}
