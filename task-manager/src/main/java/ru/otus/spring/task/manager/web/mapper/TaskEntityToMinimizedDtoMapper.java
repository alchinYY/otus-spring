package ru.otus.spring.task.manager.web.mapper;

import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskMinimizedDto;

@Component
public class TaskEntityToMinimizedDtoMapper extends AbstractConverter<TaskEntity, TaskMinimizedDto> {
    @Override
    protected TaskMinimizedDto convert(TaskEntity taskEntity) {
        return new TaskMinimizedDto(
                taskEntity.getId(),
                taskEntity.getKey(),
                taskEntity.getSummary(),
                taskEntity.getDescription(),
                taskEntity.getTaskStatus().getName()
        );
    }
}
