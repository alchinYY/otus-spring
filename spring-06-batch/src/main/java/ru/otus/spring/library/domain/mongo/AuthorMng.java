package ru.otus.spring.library.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "authors")
@FieldNameConstants
public class AuthorMng {

    @Id
    private String id;

    private String name;

    public AuthorMng(String id) {
        this.id = id;
    }
}
