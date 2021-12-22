package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class Author {

    private long id;
    private String name;

    public Author(String name) {
        this.name = name;
    }

    public Author(Long id) {
        this.id = id;
    }
}
