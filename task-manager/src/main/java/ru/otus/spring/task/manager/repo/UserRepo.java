package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @EntityGraph(value = "users-entity-graph", type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserEntity> findByLogin(String login);
}
