package ru.otus.spring.task.manager.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskCommentDto;

@Component
@RequiredArgsConstructor
public class TaskCommentEntityToDtoMapper extends AbstractConverter<TaskCommentEntity, TaskCommentDto> {

    private final TaskUserEntityToDtoMapper taskUserEntityToDtoMapper;

    @Override
    protected TaskCommentDto convert(TaskCommentEntity entity) {
        return TaskCommentDto.builder()
                .id(entity.getId())
                .author(taskUserEntityToDtoMapper.convert(entity.getAuthor()))
                .date(entity.getDate())
                .body(entity.getBody())
                .build();
    }
}
