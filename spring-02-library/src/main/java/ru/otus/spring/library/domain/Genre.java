package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Genre {

    private long id;
    private String name;

    public Genre(String name){
        this.name = name;
    }

}
