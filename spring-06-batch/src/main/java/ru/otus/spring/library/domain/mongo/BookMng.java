package ru.otus.spring.library.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class BookMng {

    @Id
    private String id;

    private String name;

    private List<String> authors;
    private String genre;

    public BookMng(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
