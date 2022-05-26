package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.ProjectEntity;

import java.util.Optional;

@Repository
public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByKey(String key);
}
