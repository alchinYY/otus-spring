package ru.otus.spring.task.manager.web.dto.resp;

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

    private Long id;

    private LocalDateTime date;

    private TaskUserDto author;

    private String body;

}
