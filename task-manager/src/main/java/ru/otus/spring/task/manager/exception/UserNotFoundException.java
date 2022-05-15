package ru.otus.spring.task.manager.exception;

public class UserNotFoundException extends TaskManagerException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
