package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    private long id;
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
