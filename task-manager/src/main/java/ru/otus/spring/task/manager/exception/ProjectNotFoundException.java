package ru.otus.spring.task.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ProjectNotFoundException extends TaskManagerException {
    public ProjectNotFoundException(String message) {
        super(message);
    }
}
