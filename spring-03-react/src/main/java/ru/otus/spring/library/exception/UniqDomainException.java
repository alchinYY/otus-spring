package ru.otus.spring.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UniqDomainException extends LibraryException {
    private static final String UNIQUE_MESSAGE_EXCEPTION = "This name \"%s\" not unique";

    public UniqDomainException(Object name){
        super(String.format(UNIQUE_MESSAGE_EXCEPTION, name));
    }

    public UniqDomainException(Object name, Throwable cause){
        super(String.format(UNIQUE_MESSAGE_EXCEPTION, name), cause);
    }
}
