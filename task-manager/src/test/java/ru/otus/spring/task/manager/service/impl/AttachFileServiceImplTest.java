package ru.otus.spring.task.manager.service.impl;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.task.manager.exception.AttachNotFoundException;
import ru.otus.spring.task.manager.model.AttachmentEntity;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.model.enumerates.StatusEntity;
import ru.otus.spring.task.manager.repo.AttachmentRepo;
import ru.otus.spring.task.manager.service.AttachFileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {"spring.liquibase.enabled=false", "attachment.location=" + AttachFileServiceImplTest.DIR_FOR_FILE_SAVE})
@Import(AttachFileServiceImpl.class)
class AttachFileServiceImplTest {

    public static final String DIR_FOR_FILE_SAVE = "location";

    private static final Long ATTACHMENT_NOT_DELETED_ID = 1L;
    private static final Long ATTACHMENT_DELETED_ID = 2L;
    private static final String OWNER_LOGIN = "login";
    private static final String RANDOM_USER_LOGIN = "random";
    private static final String TEST_LOCATION_DIR = "TEST_LOCATION_DIR";
    private static final String FILE_PATH = "src/test/resources/";
    private static final String FILE_NAME = "attachment_test.txt";

    @Autowired
    private AttachFileService attachFileService;

    @MockBean
    private AttachmentRepo attachmentRepo;

    @Test
    @EnabledOnOs({ OS.LINUX, OS.MAC })
    void uploadFile() throws IOException {
        String location = DIR_FOR_FILE_SAVE;
        UserEntity userEntity = new UserEntity("login");
        MultipartFile multipartFile = createMultipartFile(FILE_PATH + FILE_NAME, FILE_NAME);
        AttachmentEntity attachForSave = new AttachmentEntity(
                location + "/" +  multipartFile.getOriginalFilename(), userEntity, multipartFile.getOriginalFilename()
        );

        when(attachmentRepo.save(any())).thenReturn(attachForSave);

        AttachmentEntity entity = attachFileService.uploadFile(location, userEntity, multipartFile);

        assertThat(entity).isEqualTo(attachForSave);
        File file = new File(DIR_FOR_FILE_SAVE);
        assertThat(file)
                .exists()
                .isDirectory()
                .isNotEmptyDirectory();
        FileUtils.forceDelete(file);
    }

    @Test
    void getById() {

        when(attachmentRepo.findByIdAndStatusIsNotDeleted(ATTACHMENT_NOT_DELETED_ID))
                .thenReturn(Optional.of(createAttachment(ATTACHMENT_NOT_DELETED_ID, StatusEntity.ACTIVE)));
        assertThat(attachFileService.getById(ATTACHMENT_NOT_DELETED_ID))
                .isNotNull()
                .extracting(AttachmentEntity::getId, AttachmentEntity::getStatus)
                .contains(ATTACHMENT_NOT_DELETED_ID, StatusEntity.ACTIVE);
        verify(attachmentRepo, times(1)).findByIdAndStatusIsNotDeleted(any());
    }

    @Test
    void getById_whenStatusDeleted() {

        when(attachmentRepo.findByIdAndStatusIsNotDeleted(ATTACHMENT_DELETED_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(AttachNotFoundException.class)
                .isThrownBy(() -> attachFileService.getById(ATTACHMENT_DELETED_ID));
    }

    @Test
    void getByIdSuperMode() {
        AttachmentEntity attachmentEntity = createAttachment(ATTACHMENT_DELETED_ID, StatusEntity.DELETED);
        when(attachmentRepo.findById(ATTACHMENT_DELETED_ID)).thenReturn(Optional.of(attachmentEntity));
        assertThat(attachFileService.getByIdSuperMode(ATTACHMENT_DELETED_ID))
                .isNotNull()
                .extracting(AttachmentEntity::getId, AttachmentEntity::getStatus)
                .contains(ATTACHMENT_DELETED_ID, StatusEntity.DELETED);
    }

    @Test
    void delete() {
        AttachmentEntity attachmentEntity = createAttachment(ATTACHMENT_NOT_DELETED_ID, StatusEntity.ACTIVE);
        when(attachmentRepo.findByIdAndStatusIsNotDeleted(ATTACHMENT_NOT_DELETED_ID)).thenReturn(Optional.of(attachmentEntity));
        assertThat(attachmentEntity.getStatus()).isNotEqualTo(StatusEntity.DELETED);

        attachFileService.delete(ATTACHMENT_NOT_DELETED_ID, new UserEntity(OWNER_LOGIN));
        assertThat(attachmentEntity.getStatus())
                .isEqualTo(StatusEntity.DELETED);

    }

    @Test
    void delete_accessDenied() {
        AttachmentEntity attachmentEntity = createAttachment(ATTACHMENT_NOT_DELETED_ID, StatusEntity.ACTIVE);
        when(attachmentRepo.findByIdAndStatusIsNotDeleted(ATTACHMENT_NOT_DELETED_ID)).thenReturn(Optional.of(attachmentEntity));
        assertThat(attachmentEntity.getStatus()).isNotEqualTo(StatusEntity.DELETED);

        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> attachFileService.delete(ATTACHMENT_NOT_DELETED_ID, new UserEntity(RANDOM_USER_LOGIN)));

        assertThat(attachmentEntity.getStatus())
                .isEqualTo(StatusEntity.ACTIVE);

    }

    private static AttachmentEntity createAttachment(long id, StatusEntity status) {
        AttachmentEntity attachmentEntity = new AttachmentEntity();
        attachmentEntity.setId(id);
        attachmentEntity.setUiFileName("test.txt");
        attachmentEntity.setFilePath("/data/test.txt");
        attachmentEntity.setDateOfCreation(LocalDateTime.now());
        attachmentEntity.setOwner(new UserEntity(OWNER_LOGIN));
        attachmentEntity.setStatus(status);

        return attachmentEntity;
    }

    private static MockMultipartFile createMultipartFile(String path, String name) throws IOException {
        FileInputStream inputFile = new FileInputStream(path);

        return new MockMultipartFile("file", name, "multipart/form-data", inputFile);
    }

}