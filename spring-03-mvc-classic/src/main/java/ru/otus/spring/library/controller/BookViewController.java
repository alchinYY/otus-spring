package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.dto.AuthorDto;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.GenreDto;
import ru.otus.spring.library.service.impl.AuthorService;
import ru.otus.spring.library.service.impl.BookService;
import ru.otus.spring.library.service.impl.GenreService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BookViewController {

    private static final String BOOK_LIST_URL = "/library/book";

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;
    private final ModelMapper modelMapper;

    @GetMapping(BOOK_LIST_URL)
    public String getAllBook(Model model) {
        List<BookDto> bookDtoList = bookService.getAll().stream().map(b -> modelMapper.map(b, BookDto.class)).collect(Collectors.toList());
        model.addAttribute("books", bookDtoList);
        return "books";
    }

    @GetMapping("/library/book/{id}")
    public String editForm(@PathVariable("id") long id, Model model) {
        createFormFormCreate(id, model);
        return "update-book";
    }

    @Validated
    @PostMapping("/library/book/{id}")
    public String editForm(
            @PathVariable Long id,
            @Valid @ModelAttribute("book") BookDto bookDto,
            BindingResult bindingResult,
            @RequestParam(value = "authorsIds", required = false) List<Long> authorsIds,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "error";
        }
        bookDto.setAuthors(authorsIds.stream().map(AuthorDto::new).collect(Collectors.toList()));
        bookService.updateById(id, modelMapper.map(bookDto, Book.class));

        return "redirect:" + BOOK_LIST_URL;
    }

    @PostMapping("/library/book/delete")
    public String delete(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:" + BOOK_LIST_URL;
    }

    @GetMapping("/library/book/create")
    public String createFormForBook(Model model) {
        createFormFormCreate(null, model);
        return "create-book";
    }

    @Validated
    @PostMapping("/library/book/create")
    public String createBook(
            @Valid @ModelAttribute("book") BookDto book,
            BindingResult bindingResult,
            @RequestParam(value = "authorsIds", required = false) List<Long> authorsIds, Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "error";
        }
        book.setAuthors(authorsIds.stream().map(AuthorDto::new).collect(Collectors.toList()));
        bookService.save(modelMapper.map(book, Book.class));
        return "redirect:" + BOOK_LIST_URL;
    }


    private void createFormFormCreate(Long id, Model model) {
        BookDto bookDto = Optional.ofNullable(id)
                .map(bookId -> modelMapper.map(bookService.getById(bookId), BookDto.class))
                .orElse(new BookDto());

        model.addAttribute("book", bookDto);
        model.addAttribute("genres",
                genreService.getAll().stream()
                        .map(g -> new GenreDto(g.getId(), g.getName()))
                        .collect(Collectors.toList())
        );
        model.addAttribute("authors",
                authorService.getAll().stream()
                        .map(a -> modelMapper.map(a, AuthorDto.class))
                        .collect(Collectors.toList())
        );
    }

}
