package ru.otus.spring.task.manager.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.impl.UserService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.UserDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UserController.URL)
@Tag(name = "user-controller", description = "Api для управления пользователями")
public class UserController {

    public static final String URL = "/api/1/user";

    private final UserService userService;
    private final ObjectMapperUtils objectMapperUtils;

    @Operation(summary = "Получить пользователя по id", responses = {
            @ApiResponse(responseCode = "200", description = ControllerMessageConstant.MSG_OK),
            @ApiResponse(responseCode = "400", description = ControllerMessageConstant.MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = ControllerMessageConstant.MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = ControllerMessageConstant.MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = ControllerMessageConstant.MSG_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = ControllerMessageConstant.MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("{id}")
    public UserDto getById(
            @Parameter(description = "id пользователя")
            @PathVariable Long id
    ) {
        return objectMapperUtils.map(userService.getById(id), UserDto.class);
    }


    @Operation(summary = "Добавение пользователя", responses = {
            @ApiResponse(responseCode = "201", description = ControllerMessageConstant.MSG_CREATED),
            @ApiResponse(responseCode = "400", description = ControllerMessageConstant.MSG_BAD_REQUEST, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = ControllerMessageConstant.MSG_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = ControllerMessageConstant.MSG_FORBIDDEN, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = ControllerMessageConstant.MSG_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public UserDto create(
            @Valid @RequestBody UserDto userDto
    ) {
        return objectMapperUtils.map(
                userService.create(objectMapperUtils.map(userDto, UserEntity.class)),
                UserDto.class);
    }

}
