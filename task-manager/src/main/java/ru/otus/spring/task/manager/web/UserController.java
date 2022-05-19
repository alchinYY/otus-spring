package ru.otus.spring.task.manager.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.service.impl.UserService;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;
import ru.otus.spring.task.manager.web.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UserController.URL)
public class UserController {

    public static final String URL = "/api/1/user";

    private final UserService userService;
    private final ObjectMapperUtils objectMapperUtils;

    @GetMapping("{id}")
    public UserDto getById(@PathVariable Long id) {
        return objectMapperUtils.map(userService.getById(id), UserDto.class);
    }


    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) {
        return objectMapperUtils.map(
                userService.create(objectMapperUtils.map(userDto, UserEntity.class)),
                UserDto.class);
    }

}
