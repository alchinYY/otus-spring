package ru.otus.spring.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private long id;
    private String name;

    public AuthorDto(Long id) {
        this.id = id;
    }
}
