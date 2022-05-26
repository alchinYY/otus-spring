package ru.otus.spring.task.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.otus.spring.task.manager.model.flow.WorkflowEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "projects")
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_counter")
    private Long taskCounter = 0L;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "flow_id")
    private WorkflowEntity workflow;

    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL})
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Set<TaskEntity> tasks = new HashSet<>();
}
