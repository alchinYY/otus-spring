package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.service.impl.GenreService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(GenreRestController.GENRE_URL)
public class GenreRestController {

    public static final String GENRE_URL = "/library/genre";

    private final GenreService genreService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<GenreDto> getAllGenres() {
        return genreService.getAll().stream().map(b -> modelMapper.map(b, GenreDto.class))
                .collect(Collectors.toList());
    }

}
