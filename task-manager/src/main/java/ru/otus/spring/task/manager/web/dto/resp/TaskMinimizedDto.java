package ru.otus.spring.task.manager.web.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskMinimizedDto {

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

}
