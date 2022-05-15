package ru.otus.spring.task.manager.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatingTaskCommentDto {

    private String body;

    private String taskKey;
}
