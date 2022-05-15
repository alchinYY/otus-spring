package ru.otus.spring.task.manager.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatingTaskDto {

    private String projectKey;

    private String summary;

    private String description;

    private String assigneeLogin;

    private String reporterLogin;

}
