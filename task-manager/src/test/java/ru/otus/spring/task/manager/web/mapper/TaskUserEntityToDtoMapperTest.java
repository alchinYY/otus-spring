package ru.otus.spring.task.manager.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.task.manager.configuration.ModelMappingConfiguration;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.resp.TaskUserDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        ModelMappingConfiguration.class,
        TaskUserEntityToDtoMapper.class
})
class TaskUserEntityToDtoMapperTest {


    @Autowired
    private ObjectMapperUtils objectMapperUtils;

    @Test
    void convert() {

        UserEntity userEntity = new UserEntity(1L, "login", "login@mail.com");

        TaskUserDto dto = objectMapperUtils.map(userEntity, TaskUserDto.class);

        assertThat(dto)
                .extracting(TaskUserDto::getId, TaskUserDto::getLogin, TaskUserDto::getEmail)
                .contains(userEntity.getId(), userEntity.getLogin(), userEntity.getEmail());

    }
}