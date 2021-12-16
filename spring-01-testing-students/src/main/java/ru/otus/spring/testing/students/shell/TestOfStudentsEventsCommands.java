package ru.otus.spring.testing.students.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.testing.students.events.Event;

import static org.springframework.shell.standard.ShellOption.NULL;

@ShellComponent
@RequiredArgsConstructor
public class TestOfStudentsEventsCommands {

    private final Event localeEvent;
    private final Event loginEvent;
    private final Event studentTestEvent;

    @ShellMethod(value = "Start test command", key = {"s", "start"})
    public String startTestingCmd(@ShellOption(value = {"-h", "--help"}) boolean help) {
        if (help) {
            return studentTestEvent.viewDescription();
        } else {
            return studentTestEvent.action();
        }
    }

    @ShellMethod(value = "Choice of localization", key = {"l", "locale"})
    public String setLocaleCmd(
            @ShellOption(value = {"-h", "--help"}) boolean help,
            @ShellOption(defaultValue = NULL) String localeKey
    ) {
        if (help) {
            return localeEvent.viewDescription();
        } else {
            return localeEvent.action(localeKey);
        }
    }

    @ShellMethod(value = "Login", key = {"u", "login"})
    public String setLoginCmd(
            @ShellOption(value = {"-h", "--help"}) boolean help,
            @ShellOption(value = {"-n", "--name"}, defaultValue = NULL) String name,
            @ShellOption(value = {"-l", "--lname"}, defaultValue = NULL) String lastName
    ) {
        if (help) {
            return loginEvent.viewDescription();
        } else {
            return loginEvent.action(name, lastName);
        }
    }
}
