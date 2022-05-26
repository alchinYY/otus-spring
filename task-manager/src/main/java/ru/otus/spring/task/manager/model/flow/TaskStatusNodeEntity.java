package ru.otus.spring.task.manager.model.flow;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.spring.task.manager.model.TaskStatusEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "task_status_nodes")
public class TaskStatusNodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "node_status_id", nullable = false)
    private TaskStatusEntity node;


    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "task_status_edges",
            joinColumns = {@JoinColumn(name = "node_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "edge_id", referencedColumnName = "id")}
    )
    private Set<TaskStatusEntity> edges = new HashSet<>();

    public TaskStatusNodeEntity(TaskStatusEntity node) {
        this.node = node;
    }

}
