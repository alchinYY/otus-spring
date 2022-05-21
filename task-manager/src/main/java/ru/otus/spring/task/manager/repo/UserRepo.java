package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u JOIN u.userRoles WHERE u.login = :login")
    Optional<UserEntity> findByLogin(String login);
}
