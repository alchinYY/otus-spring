package ru.otus.spring.task.manager.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.task.manager.configuration.ModelMappingConfiguration;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.resp.TaskCommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ModelMappingConfiguration.class,
        TaskUserEntityToDtoMapper.class,
        TaskCommentEntityToDtoMapper.class
})
class TaskCommentEntityToDtoMapperTest {

    @Autowired
    private ObjectMapperUtils objectMapperUtils;

    @Test
    void convert() {
        TaskCommentEntity taskCommentEntity = new TaskCommentEntity();
        taskCommentEntity.setId(1L);
        taskCommentEntity.setBody("body");
        taskCommentEntity.setDate(LocalDateTime.now());
        taskCommentEntity.setAuthor(new UserEntity("author"));

        TaskCommentDto dto = objectMapperUtils.map(taskCommentEntity, TaskCommentDto.class);

        assertThat(dto)
                .extracting(TaskCommentDto::getId, TaskCommentDto::getBody, TaskCommentDto::getDate)
                .contains(taskCommentEntity.getId(), taskCommentEntity.getBody(), taskCommentEntity.getDate());
    }
}
