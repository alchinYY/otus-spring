package ru.otus.spring.task.manager.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskEntityToResponseDtoMapper extends AbstractConverter<TaskEntity, TaskResponseDto> {

    private final AttachEntityToDtoMapper attachEntityToDtoMapper;
    private final TaskUserEntityToDtoMapper taskUserEntityToDtoMapper;
    private final TaskCommentEntityToDtoMapper taskCommentEntityToDtoMapper;

    @Override
    protected TaskResponseDto convert(TaskEntity taskEntity) {
        return TaskResponseDto.builder()
                .id(taskEntity.getId())
                .status(taskEntity.getTaskStatus().getName())
                .summary(taskEntity.getSummary())
                .key(taskEntity.getKey())
                .dateOfCreation(taskEntity.getDateOfCreation())
                .description(taskEntity.getDescription())
                .lastModify(taskEntity.getLastModify())
                .assignee(taskUserEntityToDtoMapper.convert(taskEntity.getAssignee()))
                .creator(taskUserEntityToDtoMapper.convert(taskEntity.getCreator()))
                .reporter(taskUserEntityToDtoMapper.convert(taskEntity.getReporter()))
                .comments(
                        Optional.ofNullable(taskEntity.getComments())
                                .orElse(Collections.emptySet())
                                .stream().map(taskCommentEntityToDtoMapper::convert).collect(Collectors.toList())
                )
                .attachments(
                        Optional.ofNullable(taskEntity.getAttachments())
                                .orElse(Collections.emptySet())
                                .stream().map(attachEntityToDtoMapper::convert)
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
