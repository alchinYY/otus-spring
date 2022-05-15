package ru.otus.spring.task.manager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.task.manager.exception.AttachFileSaveException;
import ru.otus.spring.task.manager.exception.AttachNotFoundException;
import ru.otus.spring.task.manager.model.AttachmentEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.model.enumerates.StatusEntity;
import ru.otus.spring.task.manager.repo.AttachmentRepo;
import ru.otus.spring.task.manager.service.AttachFileService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachFileServiceImpl implements AttachFileService {

    @Value("${attachment.location}")
    private File location;

    @Value("#{T(java.time.format.DateTimeFormatter).ofPattern( 'yyyy_MM_dd-HH_mm_ss_SSS__' )}")
    private DateTimeFormatter dateTimeFormatter;

    private final AttachmentRepo attachmentRepo;

    @PostConstruct
    public void init() {
    }

    @Override
    public AttachmentEntity uploadFile(String directory, UserEntity userEntity, MultipartFile file) {
        log.info("save file with name {}", file.getName());
        try {
            String fileName = Objects.requireNonNull(file.getOriginalFilename());
            File saveFile = new File(checkLocationDir(directory), LocalDateTime.now().format(dateTimeFormatter) + fileName);
            Files.copy(file.getInputStream(), Paths.get(saveFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
            AttachmentEntity attachmentEntity = new AttachmentEntity(saveFile.getPath(), userEntity, fileName);
            return attachmentRepo.save(attachmentEntity);
        } catch (IOException ex) {
            throw new AttachFileSaveException(ex.getMessage(), ex.getCause());
        }
    }

    @Override
    public AttachmentEntity getById(long id) {
        return attachmentRepo.findById(id)
                .orElseThrow(
                        () -> new AttachNotFoundException(String.format("Attachment with \"%s\" not found", id))
                );
    }

    @Override
    @Transactional
    public void delete(long id, UserEntity userEntity) {
        AttachmentEntity attachmentEntity = getById(id);
        if(userEntity.getLogin().equals(attachmentEntity.getOwner().getLogin())) {
            getById(id).setStatus(StatusEntity.deleted);
        } else {
            throw new AccessDeniedException("user \"" + userEntity.getLogin() + "\" not owner");
        }

    }

    private File checkLocationDir(String task) throws IOException {
        File file = new File(location, task);
        FileUtils.forceMkdir(file);
        return file;
    }

}
