package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldNameConstants
@Document(value = "genres")
public class Genre {

    public static final String SEQUENCE_NAME = "genres_sequence";

    @Id
    private String id;

    private String name;

    public Genre(String id) {
        this.id = id;
    }
}