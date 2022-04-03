package ru.otus.spring.library.domain.mongo;

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
public class GenreMng {

    @Id
    private String id;

    private String name;

    public GenreMng(String id) {
        this.id = id;
    }
}
