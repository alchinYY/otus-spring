package ru.otus.spring.task.manager.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.task.manager.configuration.ModelMappingConfiguration;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.repo.UserRepo;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.CreatingTaskDto;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ModelMappingConfiguration.class, CreatingTaskDtoToTaskEntityMapper.class})
class CreatingTaskDtoToTaskEntityMapperTest {

    private static final String TASK_DESCRIPTION = "description";
    private static final String TASK_SUMMARY = "summary";
    private static final String TASK_ASSIGNEE_LOGIN = "assignee";
    private static final String TASK_REPORTER_LOGIN = "reporter";
    private static final String PROJECT_KEY = "KEY";

    @Autowired
    private ObjectMapperUtils objectMapperUtils;

    @MockBean
    private UserRepo userRepo;

    @Test
    void testMapper() {
        CreatingTaskDto creatingTaskDto = new CreatingTaskDto(
                PROJECT_KEY, TASK_SUMMARY, TASK_DESCRIPTION, TASK_ASSIGNEE_LOGIN, TASK_REPORTER_LOGIN
        );

        when(userRepo.findByLogin(TASK_ASSIGNEE_LOGIN)).thenReturn(Optional.of(new UserEntity(TASK_ASSIGNEE_LOGIN)));
        when(userRepo.findByLogin(TASK_REPORTER_LOGIN)).thenReturn(Optional.of(new UserEntity(TASK_REPORTER_LOGIN)));

        TaskEntity taskEntity = objectMapperUtils.map(creatingTaskDto, TaskEntity.class);

        assertThat(taskEntity.getAssignee().getLogin()).isEqualTo(TASK_ASSIGNEE_LOGIN);
        assertThat(taskEntity.getReporter().getLogin()).isEqualTo(TASK_REPORTER_LOGIN);
        assertThat(taskEntity)
                .extracting(TaskEntity::getSummary, TaskEntity::getDescription)
                .contains(TASK_SUMMARY, TASK_DESCRIPTION);

        verify(userRepo, times(2)).findByLogin(anyString());
    }

}