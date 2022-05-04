package ru.otus.spring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CommentDto {

    private LocalDateTime date;

    @NotBlank(message = "{body-field-should-not-be-blank}")
    private String body;

}
