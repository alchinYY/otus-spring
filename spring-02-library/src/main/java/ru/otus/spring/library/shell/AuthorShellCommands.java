package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Author;

import java.util.List;

import static ru.otus.spring.library.shell.AuthorShellCommands.CMD_KEY;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(CMD_KEY)
public class AuthorShellCommands {
    protected static final String CMD_KEY = "author";

    private final Dao<Long, Author> authorDao;

    @ShellMethod(value = "Work with authors get", key = {CMD_KEY + " get"})
    public Author getCmd(
            @ShellOption(value = "id") long id
    ){
        return authorDao.getById(id);
    }

    @ShellMethod(value = "Work with authors all", key = {CMD_KEY + " all"})
    public List<Author> getAllCmd(){
        return authorDao.getAll();
    }

    @ShellMethod(value = "Work with authors", key = {CMD_KEY + " put"})
    public Author createCmd(String name){
        return authorDao.save(new Author(name));
    }

    @ShellMethod(value = "Work with authors, update", key = {CMD_KEY + " update"})
    public Author updateCmd(Long id, String name){
        authorDao.updateById(id, new Author(name));
        return authorDao.getById(id);
    }

    @ShellMethod(value = "Work with authors, delete", key = {CMD_KEY + " delete"})
    public String deleteCmd(Long id){
        authorDao.deleteById(id);
        return "OK";
    }
}
