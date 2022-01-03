package ru.otus.spring.library.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.spring.library.domain.Book;
import ru.otus.spring.library.domain.Comment;
import ru.otus.spring.library.service.impl.CommentService;

import java.util.List;
import java.util.Set;

@ShellComponent
@RequiredArgsConstructor
@ShellCommandGroup(CommentShellCommands.CMD_KEY)
public class CommentShellCommands {
    protected static final String CMD_KEY = "comment ";

    private final CommentService service;

    @ShellMethod(value = "Work with comment, get", key = {CMD_KEY + "get"})
    public Comment getCmd(long id){
        return service.getById(id);
    }

    @ShellMethod(value = "Work with comment, all", key = {CMD_KEY + "all"})
    public List<Comment> getAllCmd(){
        return service.getAll();
    }

    @ShellMethod(value = "Work with comment, save", key = {CMD_KEY + "save"})
    public Comment saveCmd(String body, Long bookId){
        return service.save(new Comment(body, Book.builder().id(bookId).build()));
    }

    @ShellMethod(value = "Work with comment, update", key = {CMD_KEY + "update"})
    public Comment updateCmd(Long id, String body){
        return service.updateById(id, new Comment(body));
    }

    @ShellMethod(value = "Work with comment, by book id", key = {CMD_KEY + "book"})
    public Set<Comment> getAllByBookIdCmd(Long id){
        return service.getAllByBookId(id);
    }

    @ShellMethod(value = "Work with comment, delete", key = {CMD_KEY + "delete"})
    public String deleteCmd(Long id){
        service.deleteById(id);
        return "OK";
    }
}
