package ru.otus.spring.task.manager.web.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAttachmentDto {
    @Schema(description = "Внутренний идентификатор")
    private Long id;

    @Schema(description = "Имя файла для UI интерфейса", example = "логи.log")
    private String uiFileName;

    @Schema(description = "Дата создания файла")
    private LocalDateTime dateOfCreation;

    @Schema(description = "Текущий статус файла", example = "ACTIVE")
    private String status;

    @Schema(description = "Текущий владелец файла")
    private TaskUserDto owner;

}
