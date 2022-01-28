package ru.otus.spring.library.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(value = "comments")
@FieldNameConstants
@NoArgsConstructor
public class Comment implements AbstractDomain, Persistable<Integer> {

    @Transient
    public static final String SEQUENCE_NAME = "comments_sequence";

    @Id
    private Integer id;

    @CreatedDate
    private LocalDateTime date;

    private String body;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @DBRef
    private Book book;

    public Comment(String body, LocalDateTime date) {
        this.body = body;
        this.date = date;
    }

    public Comment(String body) {
        this.body = body;
    }

    public Comment(int id, String body) {
        this.body = body;
        this.id = id;
    }

    @Override
    public boolean isNew() {
        return date == null;
    }
}
