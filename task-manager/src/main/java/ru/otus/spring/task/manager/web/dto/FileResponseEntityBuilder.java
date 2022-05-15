package ru.otus.spring.task.manager.web.dto;

import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import ru.otus.spring.task.manager.model.AttachmentEntity;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileResponseEntityBuilder {

    private FileResponseEntityBuilder() {
    }

    @SneakyThrows
    public static ResponseEntity<ByteArrayResource> createFileEntity(AttachmentEntity attachmentEntity) {
        File file = new File(attachmentEntity.getFilePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(
                Paths.get(file.getAbsolutePath()))
        );
        return ResponseEntity.ok()
                .headers(createContentDispositionHeader(attachmentEntity.getUiFileName()))
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    private static HttpHeaders createContentDispositionHeader(String fileName) {
        HttpHeaders httpHeaders = new HttpHeaders();
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
        httpHeaders.setContentDisposition(contentDisposition);

        return httpHeaders;
    }

}
