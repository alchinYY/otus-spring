package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private long id;
    private String name;
    private List<Author> authors;
    private Genre genre;


    public Book(long id, String name){
        this.id = id;
        this.name = name;
    }
}
