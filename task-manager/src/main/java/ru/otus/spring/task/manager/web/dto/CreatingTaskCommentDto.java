package ru.otus.spring.task.manager.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "comment-dto", description = "Объект - комментарий")
public class CreatingTaskCommentDto {

    @Schema(description = "Тело комментария", example = "Ооо, отличная задача, отложу на 3 спринта")
    @Size(min = 3, message = "Слишком короткий комментарий")
    @NotNull
    private String body;

    @Schema(description = "Ключ задачи", example = "KEY-1")
    @Pattern(regexp = "[A-Z]+-[0-9]+")
    private String taskKey;
}
