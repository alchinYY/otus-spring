package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.flow.TaskStatusNodeEntity;

import java.util.Optional;

@Repository
public interface TaskStatusNodeRepo extends JpaRepository<TaskStatusNodeEntity, Long> {

    @Query("SELECT node FROM ProjectEntity pe JOIN pe.workflow wf JOIN wf.statusNodes node JOIN node.node ts " +
            "WHERE ts.id = :taskStatusId and pe.id = :projectId")
    Optional<TaskStatusNodeEntity> getByNodeTaskStatusIdAndProjectId(Long taskStatusId, Long projectId);

}
