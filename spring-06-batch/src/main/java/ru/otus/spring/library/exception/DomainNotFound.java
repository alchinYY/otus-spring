package ru.otus.spring.library.exception;

public class DomainNotFound extends RuntimeException {

    private static final String DOMAIN_NOT_FOUND_MESSAGE = "Domain \"%s\" not found";

    public DomainNotFound(Object domain, Throwable cause){
        super(String.format(DOMAIN_NOT_FOUND_MESSAGE, domain), cause);
    }

    public DomainNotFound(Object domain){
        super(String.format(DOMAIN_NOT_FOUND_MESSAGE, domain));
    }
}
