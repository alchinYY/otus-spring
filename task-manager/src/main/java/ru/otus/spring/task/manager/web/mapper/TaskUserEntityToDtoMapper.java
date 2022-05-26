package ru.otus.spring.task.manager.web.mapper;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskUserDto;

@Component
public class TaskUserEntityToDtoMapper extends AbstractConverter<UserEntity, TaskUserDto> {
    @Override
    protected TaskUserDto convert(UserEntity userEntity) {
        return new TaskUserDto(
                userEntity.getId(), userEntity.getLogin(), userEntity.getEmail()
        );
    }
}
