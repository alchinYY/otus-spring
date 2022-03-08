package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.service.impl.BookService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static ru.otus.spring.library.controller.BookRestController.BOOK_URL;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(BOOK_URL)
public class BookRestController {

    public static final String BOOK_URL = "/library/book";

    private final BookService bookService;
    private final ModelMapper modelMapper;

    @GetMapping
    public List<BookDto> getAllBook() {
        return bookService.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return convertToDto(bookService.getById(id));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") long id) {
        bookService.deleteById(id);
    }

    @Validated
    @PostMapping
    public BookDto createBook(@Valid @RequestBody BookDto book) {
        return convertToDto(bookService.save(convertToEntity(book)));
    }

    private BookDto convertToDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private Book convertToEntity(BookDto book) {
        return modelMapper.map(book, Book.class);
    }
}
