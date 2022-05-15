package ru.otus.spring.task.manager.web.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.TaskCommentEntity;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.web.dto.resp.TaskAttachmentDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskCommentDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskUserDto;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskEntityToResponseDto extends AbstractConverter<TaskEntity, TaskResponseDto> {

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
                .assignee(mapUserToDto(taskEntity.getAssignee()))
                .creator(mapUserToDto(taskEntity.getCreator()))
                .reporter(mapUserToDto(taskEntity.getReporter()))
                .comments(taskEntity.getComments().stream().map(this::toTaskCommentDto).collect(Collectors.toList()))
                .attachments(taskEntity.getAttachments().stream().map(a ->
                        new TaskAttachmentDto(a.getId(), a.getUiFileName(), a.getDateOfCreation(), a.getStatus().name(), mapUserToDto(a.getOwner())))
                        .collect(Collectors.toSet()))
                .build();
    }

    private TaskCommentDto toTaskCommentDto(TaskCommentEntity entity) {
        return TaskCommentDto.builder()
                .id(entity.getId())
                .author(mapUserToDto(entity.getAuthor()))
                .date(entity.getDate())
                .body(entity.getBody())
                .build();
    }

    private TaskUserDto mapUserToDto(UserEntity userEntity) {
        return new TaskUserDto(
                userEntity.getId(), userEntity.getLogin(), userEntity.getEmail()
        );
    }
}
