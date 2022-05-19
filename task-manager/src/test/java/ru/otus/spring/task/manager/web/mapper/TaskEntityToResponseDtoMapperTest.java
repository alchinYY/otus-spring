package ru.otus.spring.task.manager.web.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import ru.otus.spring.task.manager.configuration.ModelMappingConfiguration;
import ru.otus.spring.task.manager.model.*;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.UserDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskAttachmentDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskResponseDto;
import ru.otus.spring.task.manager.web.dto.resp.TaskUserDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
        ModelMappingConfiguration.class,
        TaskEntityToResponseDtoMapper.class,
        AttachEntityToDtoMapper.class,
        TaskUserEntityToDtoMapper.class
})
class TaskEntityToResponseDtoMapperTest {

    public static final Long TASK_ID = 1L;
    public static final String TASK_KEY = "KEY-1";
    public static final String TASK_SUMMARY = "summary";
    public static final String TASK_DESCRIPTION = "description";
    public static final TaskStatusEntity TASK_STATUS = new TaskStatusEntity(1L, "to_do");
    public static final UserEntity TASK_ASSIGNEE = new UserEntity("assignee");
    public static final UserEntity TASK_REPORTER = new UserEntity("reporter");
    public static final UserEntity TASK_CREATOR = new UserEntity("creator");
    public static final LocalDateTime TASK_DATE_CREATION = LocalDateTime.of(1999, 1, 1, 1, 1);
    public static final LocalDateTime TASK_DATE_MODIFY = LocalDateTime.of(1999, 1, 1, 1, 1);

    public static final AttachmentEntity TASK_ATTACH = new AttachmentEntity("/path", TASK_REPORTER, "file.txt");

    @Autowired
    private ObjectMapperUtils objectMapperUtils;

    @MockBean
    private TaskCommentEntityToDtoMapper taskCommentEntityToDtoMapper;

    @Test
    void convertEmptyCommentsAndEmptyAttach() {
        TaskEntity taskEntity = createTaskEntityDto();
        TaskResponseDto taskResponseDto = objectMapperUtils.map(taskEntity, TaskResponseDto.class);
        TaskResponseDto expectedDto = createExpectedDto();
        assertThat(taskResponseDto).isEqualTo(expectedDto);
    }

    @Test
    void testMapperEmptyCommentsAndNotEmptyAttach() {
        TaskEntity taskEntity = createTaskEntityDtoWithAttach();
        TaskResponseDto taskResponseDto = objectMapperUtils.map(taskEntity, TaskResponseDto.class);
        TaskResponseDto expectedDto = createExpectedDtoWithAttach();
        assertThat(taskResponseDto).isEqualTo(expectedDto);
    }

    private TaskEntity createTaskEntityDtoWithAttach() {
        TaskEntity taskEntity = createTaskEntityDto();
        taskEntity.setAttachments(new HashSet<>());
        taskEntity.getAttachments().add(TASK_ATTACH);
        return taskEntity;
    }

    private TaskResponseDto createExpectedDtoWithAttach() {
        TaskResponseDto taskResponseDto = createExpectedDto();
        taskResponseDto.setAttachments(new HashSet<>());
        taskResponseDto.getAttachments().add(
                new TaskAttachmentDto(
                        TASK_ATTACH.getId(),
                        TASK_ATTACH.getUiFileName(),
                        TASK_ATTACH.getDateOfCreation(),
                        TASK_ATTACH.getStatus().name(),
                        new TaskUserDto(TASK_ATTACH.getOwner().getLogin())));
        return taskResponseDto;
    }

    private TaskEntity createTaskEntityDto() {
        return TaskEntity.builder()
                .id(TASK_ID)
                .key(TASK_KEY)
                .summary(TASK_SUMMARY)
                .description(TASK_DESCRIPTION)
                .taskStatus(TASK_STATUS)
                .assignee(TASK_ASSIGNEE)
                .creator(TASK_CREATOR)
                .reporter(TASK_REPORTER)
                .dateOfCreation(TASK_DATE_CREATION)
                .lastModify(TASK_DATE_MODIFY)
                .build();
    }

    private TaskResponseDto createExpectedDto() {
        return TaskResponseDto.builder()
                .id(TASK_ID)
                .key(TASK_KEY)
                .summary(TASK_SUMMARY)
                .description(TASK_DESCRIPTION)
                .status(TASK_STATUS.getName())
                .assignee(new TaskUserDto(TASK_ASSIGNEE.getLogin()))
                .creator(new TaskUserDto(TASK_CREATOR.getLogin()))
                .reporter(new TaskUserDto(TASK_REPORTER.getLogin()))
                .dateOfCreation(TASK_DATE_CREATION)
                .lastModify(TASK_DATE_MODIFY)
                .attachments(Collections.emptySet())
                .comments(Collections.emptyList())
                .build();
    }

}
