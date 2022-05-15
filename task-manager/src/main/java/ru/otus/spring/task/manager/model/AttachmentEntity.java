package ru.otus.spring.task.manager.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.otus.spring.task.manager.model.enumerates.StatusEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@NoArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "ui_file_name")
    private String uiFileName;

    @CreatedDate
    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEntity status = StatusEntity.active;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;

    public AttachmentEntity(String filePath, UserEntity owner, String uiFileName) {
        this.filePath = filePath;
        this.uiFileName = uiFileName;
        this.owner = owner;
    }

}
