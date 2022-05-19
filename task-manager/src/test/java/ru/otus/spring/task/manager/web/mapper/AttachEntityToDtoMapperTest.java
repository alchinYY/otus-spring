package ru.otus.spring.task.manager.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.task.manager.configuration.ModelMappingConfiguration;
import ru.otus.spring.task.manager.model.AttachmentEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.resp.TaskAttachmentDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ModelMappingConfiguration.class,
        TaskUserEntityToDtoMapper.class,
        AttachEntityToDtoMapper.class
})
class AttachEntityToDtoMapperTest {

    public static final UserEntity TASK_OWNER = new UserEntity("owner");

    @Autowired
    private ObjectMapperUtils objectMapperUtils;

    @Test
    void convert() {

        AttachmentEntity attachmentEntity = new AttachmentEntity("/path", TASK_OWNER, "file.txt");

        TaskAttachmentDto taskAttachmentDto = objectMapperUtils.map(attachmentEntity, TaskAttachmentDto.class);

        assertThat(taskAttachmentDto)
                .extracting(dto -> dto.getOwner().getLogin(), dto -> taskAttachmentDto.getUiFileName())
                .contains(TASK_OWNER.getLogin(), attachmentEntity.getUiFileName());
    }
}