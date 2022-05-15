package ru.otus.spring.task.manager.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.exception.UserNotFoundException;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.repo.UserRepo;
import ru.otus.spring.task.manager.web.dto.CreatingTaskDto;

@RequiredArgsConstructor
@Component
public class CreatingTaskDtoToTaskEntityMapper extends PropertyMap<CreatingTaskDto, TaskEntity> {

    private final UserRepo userRepo;

    @Override
    protected void configure() {

        Converter<String, UserEntity> converterUsers = new AbstractConverter<>() {
            @Override
            protected UserEntity convert(String source) {
                return userRepo.findByLogin(source)
                        .orElseThrow(() -> new UserNotFoundException(String.format("User with login \"%s\" not found", source)));
            }
        };

        using(converterUsers).map(source.getAssigneeLogin(), destination.getAssignee());
        using(converterUsers).map(source.getReporterLogin(), destination.getReporter());
    }
}
