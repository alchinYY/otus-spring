package ru.otus.spring.task.manager.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "creating-task-dto")
public class CreatingTaskDto {

    @NotNull
    @Pattern(regexp = "[A-Z]+")
    @Schema(description = "Ключ проекта", example = "KEY")
    private String projectKey;

    @NotNull
    @Schema(description = "Описание задачи", example = "Очень важная задача от архитектора")
    private String summary;

    @Schema(description = "Описание задачи", example = "описание, нужно сделалть ля-ля-ля")
    private String description;

    @NotNull
    @Pattern(regexp = "[\\w\\d]+")
    @Schema(description = "Логин пользователя", example = "user0987")
    private String assigneeLogin;

    @NotNull
    @Pattern(regexp = "[\\w\\d]+")
    @Schema(description = "Логин пользователя", example = "user0987")
    private String reporterLogin;

}
