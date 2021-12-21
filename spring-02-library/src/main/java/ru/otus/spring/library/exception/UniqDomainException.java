package ru.otus.spring.library.exception;

public class UniqDomainException extends RuntimeException {
    private static final String UNIQUE_MESSAGE_EXCEPTION = "This name \"%s\" not unique";

    public UniqDomainException(Object name){
        super(String.format(UNIQUE_MESSAGE_EXCEPTION, name));
    }

    public UniqDomainException(Object name, Throwable cause){
        super(String.format(UNIQUE_MESSAGE_EXCEPTION, name), cause);
    }
}
