package ru.otus.spring.task.manager.web.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TaskResponseDto {

    private Long id;
    private String key;
    private String summary;
    private String description;
    private String status;
    private TaskUserDto assignee;
    private TaskUserDto reporter;
    private TaskUserDto creator;
    private LocalDateTime dateOfCreation;
    private LocalDateTime lastModify;
    private List<TaskCommentDto> comments = new ArrayList<>();
    private Set<TaskAttachmentDto> attachments = new HashSet<>();
}
