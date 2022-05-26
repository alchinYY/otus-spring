package ru.otus.spring.task.manager.model.flow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.otus.spring.task.manager.model.TaskStatusEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task_workflow")
@Data
@NoArgsConstructor
public class WorkflowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @JoinColumn(name = "flow_id", referencedColumnName = "id")
    private List<TaskStatusNodeEntity> statusNodes = new ArrayList<>();


    @ManyToOne
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "start_status_id")
    private TaskStatusEntity startStatus;
}
