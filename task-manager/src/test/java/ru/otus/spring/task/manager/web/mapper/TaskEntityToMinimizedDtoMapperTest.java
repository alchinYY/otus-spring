package ru.otus.spring.task.manager.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.task.manager.configuration.ModelMappingConfiguration;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.TaskStatusEntity;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.resp.TaskMinimizedDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ModelMappingConfiguration.class,
        TaskEntityToMinimizedDtoMapper.class
})
class TaskEntityToMinimizedDtoMapperTest {

    @Autowired
    private ObjectMapperUtils objectMapperUtils;


    @Test
    void convert() {
        TaskStatusEntity taskStatusEntity = new TaskStatusEntity(1L, "todo");

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setTaskStatus(taskStatusEntity);
        taskEntity.setSummary("summary");
        taskEntity.setDescription("description");

        TaskMinimizedDto taskMinimizedDto = objectMapperUtils.map(taskEntity, TaskMinimizedDto.class);

        assertThat(taskMinimizedDto)
                .extracting(TaskMinimizedDto::getId, TaskMinimizedDto::getStatus, TaskMinimizedDto::getDescription)
                .contains(taskEntity.getId(), taskEntity.getTaskStatus().getName(), taskEntity.getDescription());
    }
}