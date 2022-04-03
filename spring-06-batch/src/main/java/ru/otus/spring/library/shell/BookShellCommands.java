package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.library.domain.sql.Author;
import ru.otus.spring.library.domain.sql.Book;
import ru.otus.spring.library.domain.sql.Genre;
import ru.otus.spring.library.service.EntityService;

import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(BookShellCommands.CMD_KEY)
public class BookShellCommands {

    protected static final String CMD_KEY = "book ";
    private static final String AUTHOR_ARRAY_STRING_FORMAT = "(\\d*;?)*";
    private static final String AUTHOR_ARRAY_STRING_EXCEPTION_MESSAGE = "--authors-id should match 1;2;3";

    private final EntityService<Book> bookService;

    @ShellMethod(value = "Work with books, get", key = {CMD_KEY + "get"})
    public Book getCmd(long id) {
        return bookService.getById(id);
    }

    @ShellMethod(value = "Work with books all", key = {CMD_KEY + "all"})
    public List<Book> getAllCmd() {
        return bookService.getAll();
    }

    @ShellMethod(value = "Work with books, create", key = {CMD_KEY + "create"})
    public Book createCmd(
            String name,
            Long genreId,
            @Pattern(regexp = AUTHOR_ARRAY_STRING_FORMAT, message = AUTHOR_ARRAY_STRING_EXCEPTION_MESSAGE) String authorsId
    ) {
       return bookService.save(createBookBody(null, name, genreId, authorsId));
    }

    @ShellMethod(value = "Work with books, update", key = {CMD_KEY + "update"})
    public Book updateCmd(
            Long bookId,
            String name,
            Long genreId,
            @Pattern(regexp = AUTHOR_ARRAY_STRING_FORMAT, message = AUTHOR_ARRAY_STRING_EXCEPTION_MESSAGE) String authorsId
    ) {
        return bookService.updateById(bookId, createBookBody(bookId, name, genreId, authorsId));
    }

    @ShellMethod(value = "Work with books, delete", key = {CMD_KEY + "delete"})
    public String deleteCmd(Long id) {
        bookService.deleteById(id);
        return "OK";
    }


    private Book createBookBody(Long bookId, String name, Long genreId, String authorsIdList) {
        Set<Author> authors = Arrays.stream(authorsIdList.split(";"))
                .map(Long::parseLong)
                .map(Author::new)
                .collect(Collectors.toSet());

        return Book.builder()
                .id(bookId)
                .name(name)
                .authors(authors)
                .genre(new Genre(genreId))
                .build();
    }
}
