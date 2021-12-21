package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.service.BookService;

import javax.validation.constraints.Pattern;
import java.util.List;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(BookShellCommands.CMD_KEY)
public class BookShellCommands {
    protected static final String CMD_KEY = "book";

    private final BookService bookService;

    @ShellMethod(value = "Work with books, get", key = {CMD_KEY + " get"})
    public Book getCmd(@ShellOption(value = "id") long id) {
        return bookService.getById(id);
    }

    @ShellMethod(value = "Work with books all", key = {CMD_KEY + " all"})
    public List<Book> getAllCmd() {
        return bookService.getAll();
    }

    @ShellMethod(value = "Work with books, create", key = {CMD_KEY + " create"})
    public Book createCmd(
            String name,
            Long genreId,
            @Pattern(regexp = "(\\d*;?)*", message = "--authors-id should match 1;2;3") String authorsId
    ) {
       return bookService.create(name, genreId, authorsId);
    }

    @ShellMethod(value = "Work with books, update", key = {CMD_KEY + " update"})
    public Book updateCmd(
            Long bookId,
            String name,
            Long genreId,
            @Pattern(regexp = "(\\d*;?)*", message = "--authors-id should match 1;2;3") String authorsId
    ) {
        return bookService.updateById(bookId, name, genreId, authorsId);
    }

    @ShellMethod(value = "Work with books, delete", key = {CMD_KEY + " delete"})
    public String deleteCmd(Long id) {
        bookService.deleteById(id);
        return "OK";
    }
}
