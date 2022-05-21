package ru.otus.spring.task.manager.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Tag(name = "user-dto")
public class UserDto {

    private Long id;

    @Pattern(regexp = "[\\w\\d]+")
    @Schema(description = "Логин пользователя", example = "user0987")
    private String login;

    @Email
    @Schema(description = "Email пользователя", example = "user0987@mail.com")
    private String email;

    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Schema(description = "Пароль пользователя", example = "secret321A")
    private String password;
}
