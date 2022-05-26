package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.TaskEntity;
import ru.otus.spring.task.manager.model.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<TaskEntity, Long> {

    @EntityGraph(value = "tasks-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT te FROM TaskEntity te LEFT OUTER JOIN te.attachments a ON a.status <> 'DELETED' LEFT JOIN te.comments com WHERE te.key = :key order by com.id asc")
    Optional<TaskEntity> findByKey(String key);

    @EntityGraph(value = "tasks-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<TaskEntity> findAllEntityByKey(String key);

    @EntityGraph(value = "tasks-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    List<TaskEntity> findByAssignee(UserEntity assignee);

}
