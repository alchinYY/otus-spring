package ru.otus.spring.task.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AttachNotFoundException extends TaskManagerException {

    public static final String MESSAGE = "Attachment with id \"%s\" not found";

    public AttachNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }

}
