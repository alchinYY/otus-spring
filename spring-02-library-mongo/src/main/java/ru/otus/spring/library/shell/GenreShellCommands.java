package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.service.EntityService;

import java.util.List;

import static ru.otus.spring.library.shell.GenreShellCommands.CMD_KEY;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(CMD_KEY)
public class GenreShellCommands {
    protected static final String CMD_KEY = "genre ";

    private final EntityService<Genre> service;

    @ShellMethod(value = "Work with genres, get", key = {CMD_KEY + "get"})
    public Genre getCmd(int id){
        return service.getById(id);
    }

    @ShellMethod(value = "Work with genres, all", key = {CMD_KEY + "all"})
    public List<Genre> getAllCmd(){
        return service.getAll();
    }


    @ShellMethod(value = "Work with genres, create", key = {CMD_KEY + "create"})
    public Genre createCmd(String name){
        return service.save(new Genre(name));
    }

    @ShellMethod(value = "Work with genres, update", key = {CMD_KEY + "update"})
    public Genre updateCmd(int id, String name){
        return service.updateById(id, new Genre(name));
    }

    @ShellMethod(value = "Work with genres, delete", key = {CMD_KEY + "delete"})
    public String deleteCmd(Integer id){
        service.deleteById(id);
        return "OK";
    }
}
