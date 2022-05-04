package ru.otus.spring.library.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDateTime date;

    @Column(name = "body", nullable = false, length = 500)
    private String body;


    public Comment(String body){
        this.body = body;
    }

    public Comment(Long id, String body){
        this.body = body;
        this.id = id;
    }

}
