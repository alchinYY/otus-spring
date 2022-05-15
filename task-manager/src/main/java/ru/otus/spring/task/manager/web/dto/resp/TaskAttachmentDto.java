package ru.otus.spring.task.manager.web.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskAttachmentDto {

    private Long id;

    private String uiFileName;

    private LocalDateTime dateOfCreation;

    private String status;

    private TaskUserDto owner;

}
