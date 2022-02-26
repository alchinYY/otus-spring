package ru.otus.spring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreDto {
    @NotNull(message = "{genre-should-not-be-null}")
    private String id;
    private String name;
}
