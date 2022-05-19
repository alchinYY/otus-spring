package ru.otus.spring.task.manager.web.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.TaskStatusEntity;
import ru.otus.spring.task.manager.service.TaskService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.admin.TaskAdminController;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskAdminController.class)
class TaskAdminControllerSecurityTest {

    private static final String TASK_KEY = "KEY-1";
    private static final Long TASK_STATUS_TODO_ID = 1L;
    private static final Long TASK_STATUS_IN_PROGRESS_ID = 2L;
    private static final TaskStatusEntity STATUS_TODO = new TaskStatusEntity(TASK_STATUS_TODO_ID, "todo");
    private static final TaskStatusEntity STATUS_IN_PROGRESS = new TaskStatusEntity(TASK_STATUS_IN_PROGRESS_ID, "in_progress");

    @MockBean
    private TaskService taskService;
    @MockBean
    private ObjectMapperUtils objectMapperUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;


    @Test
    @WithMockUser(username = "user", authorities = "ADMIN")
    void setTaskStatus() throws Exception {

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setKey(TASK_KEY);
        taskEntity.setTaskStatus(new TaskStatusEntity(STATUS_TODO.getId(), STATUS_TODO.getName()));

        TaskResponseDto taskResponseDto = new TaskResponseDto();
        taskResponseDto.setKey(TASK_KEY);
        taskResponseDto.setStatus(STATUS_IN_PROGRESS.getName());

        when(taskService.setStatus(any(), any())).thenReturn(mock(TaskEntity.class));
        when(objectMapperUtils.map(any(), any())).thenReturn(taskResponseDto);
        mvc.perform(post(TaskAdminController.URL + "/{key}/status/id/{statusId}", TASK_KEY, TASK_STATUS_TODO_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskResponseDto)));

    }

    @Test
    void setTaskStatus_unauthorized() throws Exception {
        mvc.perform(post(TaskAdminController.URL + "/{key}/status/id/{statusId}", TASK_KEY, TASK_STATUS_TODO_ID))
                .andExpect(status().is(401));
    }

    @Test
    @WithMockUser(username = "user")
    void setTaskStatus_otherAuthorities() throws Exception {
        mvc.perform(post(TaskAdminController.URL + "/{key}/status/id/{statusId}", TASK_KEY, TASK_STATUS_TODO_ID))
                .andExpect(status().is(403));
    }
}
