package ru.otus.spring.task.manager.web.mapper;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskCommentDto;

@Component
public class TaskCommentEntityToDto extends PropertyMap<TaskCommentEntity, TaskCommentDto> {

    @Override
    protected void configure() {
        Converter<UserEntity, String> converterUsers = new AbstractConverter<>() {
            @Override
            protected String convert(UserEntity source) {
                return source.getLogin();
            }
        };

        using(converterUsers).map(source.getAuthor(), destination.getAuthor());
    }
}
