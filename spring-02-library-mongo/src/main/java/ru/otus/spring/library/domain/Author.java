package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "authors")
@FieldNameConstants
public class Author implements AbstractDomain {

    @Transient
    public static final String SEQUENCE_NAME = "authors_sequence";

    @Id
    private Integer id;

    private String name;

    public Author(String name) {
        this.name = name;
    }

    public Author(Integer id) {
        this.id = id;
    }
}
