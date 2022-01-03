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
@NamedEntityGraph(name = "comment-entity-graph",
        attributeNodes = @NamedAttributeNode("book"))
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDateTime date;

    @Column(name = "body", nullable = false, length = 500)
    private String body;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Override
    public String toString() {
        return "Comment(id = " + id + ", " +
                "date = " + date + ", " +
                "body = \"" + body + "\"," +
                "book.id = " + book.getId() + ", " +
                "book.name = " + book.getName() + ")";
    }

    public Comment(String body){
        this.body = body;
    }

    public Comment(Long id, String body){
        this.body = body;
        this.id = id;
    }

    public Comment(String body, Book book){
        this.body = body;
        this.book = book;
    }
}
