package ru.otus.spring.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DomainNotFound extends LibraryException {

    private static final String DOMAIN_NOT_FOUND_MESSAGE = "Domain \"%s\" not found";

    public DomainNotFound(Object domain, Throwable cause){
        super(String.format(DOMAIN_NOT_FOUND_MESSAGE, domain), cause);
    }

    public DomainNotFound(Object domain){
        super(String.format(DOMAIN_NOT_FOUND_MESSAGE, domain));
    }
}
