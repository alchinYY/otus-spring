package ru.otus.spring.butterfly.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Butterfly {

    private String type;
    private Long index;
    private Long size;
    private boolean alive;
}
