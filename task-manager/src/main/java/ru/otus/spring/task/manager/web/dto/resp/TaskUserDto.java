package ru.otus.spring.task.manager.web.dto.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserDto {

    @Schema(description = "Внутренний идентификатор")
    private Long id;

    @Schema(description = "Логин пользователя", example = "user1")
    private String login;

    @Schema(description = "Email пользователя", example = "user1@mail.com")
    private String email;

    public TaskUserDto(String login) {
        this.login = login;
    }

}
