package ru.otus.spring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long id;

    @NotBlank(message = "{name-field-should-not-be-blank}")
    private String name;
    @NotNull(message = "{genre-should-not-be-null}")
    @Valid
    private GenreDto genre;
    private List<AuthorDto> authors;
}
