package ru.otus.spring.task.manager.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.task.manager.service.AttachFileService;
import ru.otus.spring.task.manager.web.dto.FileResponseEntityBuilder;

import static ru.otus.spring.task.manager.web.ControllerMessageConstant.*;
import static ru.otus.spring.task.manager.web.ControllerMessageConstant.MSG_INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping(AttachmentController.URL)
@Tag(name = "attach-controller", description = "Управление вложениями")
public class AttachmentController {

    public static final String URL = "/api/1/attachment";

    private final AttachFileService attachFileService;
    private final SecurityUtil securityUtil;

    @GetMapping(value = "/{id}")
    @Operation(summary = "Получение вложения", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<ByteArrayResource> loadFile(
            @Parameter(description = "id вложения", example = "2") @PathVariable long id
    ) {
        return FileResponseEntityBuilder.createFileEntity(attachFileService.getById(id));
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Удаление вложения", responses = {
            @ApiResponse(responseCode = "200", description = MSG_OK),
            @ApiResponse(responseCode = "400", description = MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    public void deleteFile(
            @Parameter(description = "id вложения", example = "2") @PathVariable long id
    ) {
        attachFileService.delete(id, securityUtil.getCurrentUser());
    }
}
