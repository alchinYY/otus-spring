package ru.otus.spring.task.manager.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.task.manager.service.AttachFileService;
import ru.otus.spring.task.manager.web.dto.FileResponseEntityBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/1/attachment")
public class AttachmentController {

    private final AttachFileService attachFileService;
    private final SecurityUtil securityUtil;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ByteArrayResource> loadFile(@PathVariable long id) {
        return FileResponseEntityBuilder.createFileEntity(attachFileService.getById(id));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteFile(@PathVariable long id) {
        attachFileService.delete(id, securityUtil.getCurrentUser());
    }
}
