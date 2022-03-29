package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.library.domain.sql.Author;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

import static ru.otus.spring.library.shell.AuthorShellCommands.CMD_KEY;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(CMD_KEY)
public class AuthorShellCommands {
    protected static final String CMD_KEY = "author ";

    private final EntityService<Author> authorService;

    @ShellMethod(value = "Work with authors get", key = {CMD_KEY + "get"})
    public Author getCmd(long id){
        return authorService.getById(id);
    }

    @ShellMethod(value = "Work with authors all", key = {CMD_KEY + "all"})
    public List<Author> getAllCmd(){
        return authorService.getAll();
    }

    @ShellMethod(value = "Work with authors", key = {CMD_KEY + "put"})
    public Author createCmd(String name){
        return authorService.save(new Author(name));
    }

    @ShellMethod(value = "Work with authors, update", key = {CMD_KEY + "update"})
    public Author updateCmd(Long id, String name){
        return authorService.updateById(id, new Author(name));
    }

    @ShellMethod(value = "Work with authors, delete", key = {CMD_KEY + "delete"})
    public String deleteCmd(Long id){
        authorService.deleteById(id);
        return "OK";
    }
}
