package ru.otus.spring.task.manager.web.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserDto {
    private Long id;
    private String login;
    private String email;

    public TaskUserDto(String login) {
        this.login = login;
    }

}
