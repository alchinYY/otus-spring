package ru.otus.spring.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends LibraryException {

    private static final String MESSAGE = "User with name %s not found";

    public UserNotFoundException(String userName) {
        super(String.format(MESSAGE, userName));
    }

    public UserNotFoundException(String userName, Throwable cause) {
        super(String.format(MESSAGE, userName), cause);
    }
}
