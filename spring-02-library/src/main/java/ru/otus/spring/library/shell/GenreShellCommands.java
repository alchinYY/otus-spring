package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.library.dao.Dao;
import ru.otus.spring.library.domain.Genre;

import java.util.List;

import static ru.otus.spring.library.shell.GenreShellCommands.CMD_KEY;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(CMD_KEY)
public class GenreShellCommands {
    protected static final String CMD_KEY = "genre";

    private final Dao<Long, Genre> genreDao;

    @ShellMethod(value = "Work with genres, get", key = {CMD_KEY + " get"})
    public Genre getCmd(@ShellOption(value = "id") long id){
        return genreDao.getById(id);
    }

    @ShellMethod(value = "Work with genres, all", key = {CMD_KEY + " all"})
    public List<Genre> getAllCmd(){
        return genreDao.getAll();
    }


    @ShellMethod(value = "Work with genres, create", key = {CMD_KEY + " create"})
    public Genre createCmd(String name){
        return genreDao.save(new Genre(name));
    }

    @ShellMethod(value = "Work with genres, update", key = {CMD_KEY + " update"})
    public Genre updateCmd(Long id, String name){
        genreDao.updateById(id, new Genre(name));
        return genreDao.getById(id);
    }

    @ShellMethod(value = "Work with genres, delete", key = {CMD_KEY + " delete"})
    public String deleteCmd(Long id){
        genreDao.deleteById(id);
        return "OK";
    }
}
