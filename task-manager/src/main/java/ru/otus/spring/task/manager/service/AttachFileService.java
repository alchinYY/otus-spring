package ru.otus.spring.task.manager.service;

import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.task.manager.model.AttachmentEntity;
import ru.otus.spring.task.manager.model.UserEntity;

public interface AttachFileService {

    AttachmentEntity uploadFile(String directory, UserEntity userEntity, MultipartFile file);

    AttachmentEntity getById(long id);

    void delete(long id, UserEntity currentUser);
}
