package ru.otus.spring.task.manager.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@NoArgsConstructor
@Entity
@Data
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NamedEntityGraph(
        name = "tasks-entity-graph",
        attributeNodes = {
                @NamedAttributeNode("assignee"),
                @NamedAttributeNode("reporter"),
                @NamedAttributeNode("creator"),
                @NamedAttributeNode("taskStatus"),
                @NamedAttributeNode("attachments"),
                @NamedAttributeNode("comments")
        }
)
@EqualsAndHashCode(of = "id")
public class TaskEntity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Column(name = "summary", length = 1000, nullable = false)
    private String summary;

    @Column(name = "description", length = 5000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatusEntity taskStatus;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private UserEntity reporter;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @CreatedDate
    @Column(name = "date_of_creation", nullable = false)
    private LocalDateTime dateOfCreation;

    @LastModifiedDate
    @Column(name = "last_modify", nullable = false)
    private LocalDateTime lastModify;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Set<AttachmentEntity> attachments = new LinkedHashSet<>();

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.ALL})
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    @OrderBy("id asc")
    private Set<TaskCommentEntity> comments = new LinkedHashSet<>();
}
