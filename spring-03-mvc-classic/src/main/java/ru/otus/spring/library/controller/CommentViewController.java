package ru.otus.spring.library.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.dto.BookDto;
import ru.otus.spring.library.dto.CommentDto;
import ru.otus.spring.library.service.impl.BookService;
import ru.otus.spring.library.service.impl.CommentService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CommentViewController {

    private final CommentService commentService;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @GetMapping("/library/book/{id}/comments")
    public String getCommentsByBookId(@PathVariable Long id, Model model) {
        Book book = bookService.getById(id);
        model.addAttribute("commentsList", book.getComments().stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .collect(Collectors.toList())
        );
        model.addAttribute("book", modelMapper.map(book, BookDto.class));
        model.addAttribute("newComment", new CommentDto());
        return "comments";
    }

    @Validated
    @PostMapping("/library/book/{id}/comments")
    public String addComment(@Valid @ModelAttribute("newComment") CommentDto comment,
                             BindingResult bindingResult,
                             @PathVariable Long id, Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "error";
        }
        commentService.save(modelMapper.map(comment, Comment.class), id);
        return getCommentsByBookId(id, model);
    }
}
