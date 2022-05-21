package ru.otus.spring.task.manager.web.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TaskResponseDto {

    @Schema(description = "Внутренний идентификатор")
    private Long id;

    @Schema(description = "Ключ задачи", example = "KEY-1")
    private String key;

    @Schema(description = "Заголовок", example = "Еще одна бесполезная задача")
    private String summary;

    @Schema(description = "Описание", example = "А нужно ли оно?")
    private String description;

    @Schema(description = "Статус задачи", example = "closed")
    private String status;

    @Schema(description = "На кого назначена")
    private TaskUserDto assignee;

    @Schema(description = "Кто автор задачи")
    private TaskUserDto reporter;

    @Schema(description = "Кто реально создал задачу")
    private TaskUserDto creator;

    @Schema(description = "Дата создания")
    private LocalDateTime dateOfCreation;

    @Schema(description = "Дата последнего изменения")
    private LocalDateTime lastModify;

    @Schema(description = "Комментарии")
    private List<TaskCommentDto> comments = new ArrayList<>();

    @Schema(description = "Вложения")
    private Set<TaskAttachmentDto> attachments = new HashSet<>();
}
