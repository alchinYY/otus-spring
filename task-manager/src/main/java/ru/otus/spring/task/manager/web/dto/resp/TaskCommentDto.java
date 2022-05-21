package ru.otus.spring.task.manager.web.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCommentDto {

    @Schema(description = "Внутренний id комментария")
    private Long id;

    @Schema(description = "Дата создания файла", example = "2022-03-05T10:10:10")
    private LocalDateTime date;

    @Schema(description = "Автор комментария")
    private TaskUserDto author;

    @Schema(description = "Содержимое комментария", example = "То, что я написал")
    private String body;

}
