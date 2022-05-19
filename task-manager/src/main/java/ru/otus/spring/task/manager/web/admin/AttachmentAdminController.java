package ru.otus.spring.task.manager.web.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.task.manager.service.AttachFileService;
import ru.otus.spring.task.manager.web.dto.FileResponseEntityBuilder;

@RestController
@RequestMapping("/api/1/admin/attachment")
@RequiredArgsConstructor
public class AttachmentAdminController {

    private final AttachFileService attachFileService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ByteArrayResource> loadFile(@PathVariable long id) {
        return FileResponseEntityBuilder.createFileEntity(attachFileService.getByIdSuperMode(id));
    }

}
