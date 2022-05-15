package ru.otus.spring.task.manager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.spring.task.manager.model.AttachmentEntity;

@Repository
public interface AttachmentRepo extends JpaRepository<AttachmentEntity, Long> {

    

}
