package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.service.impl.AuthorService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AuthorRestController.AUTHOR_URL)
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    public static final String AUTHOR_URL = "/library/author";

    @GetMapping
    public List<AuthorDto> getAuthors() {
        return authorService.getAll().stream().map(a -> modelMapper.map(a, AuthorDto.class))
                .collect(Collectors.toList());
    }

}
