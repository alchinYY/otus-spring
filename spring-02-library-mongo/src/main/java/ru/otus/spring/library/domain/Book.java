package ru.otus.spring.library.domain;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class Book  implements AbstractDomain {

    @Transient
    public static final String SEQUENCE_NAME = "books_sequence";

    @Id
    private Integer id;

    private String name;

    @DBRef
    private List<Author> authors;

    @DBRef
    private Genre genre;

    @DBRef(lazy = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Comment> comments;
}
