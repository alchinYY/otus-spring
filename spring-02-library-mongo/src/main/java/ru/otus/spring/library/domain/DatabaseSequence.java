package ru.otus.spring.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "database_sequences")
@AllArgsConstructor
@Data
@FieldNameConstants
public class DatabaseSequence implements AbstractDomain {
    @Id
    private String id;

    private int seq;
}
