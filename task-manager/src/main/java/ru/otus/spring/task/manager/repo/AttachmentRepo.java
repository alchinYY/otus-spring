package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.AttachmentEntity;

import java.util.Optional;

@Repository
public interface AttachmentRepo extends JpaRepository<AttachmentEntity, Long> {

    @Query("SELECT a FROM AttachmentEntity a WHERE a.id = :id and a.status <> 'DELETED'")
    Optional<AttachmentEntity> findByIdAndStatusIsNotDeleted(Long id);

}
