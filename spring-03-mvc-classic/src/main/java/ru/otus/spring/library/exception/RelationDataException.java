package ru.otus.spring.library.exception;

public class RelationDataException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "Violation of dependencies. Possibly there is a related entity for id %s";

    public RelationDataException(Object id) {
        super(String.format(EXCEPTION_MESSAGE, id));
    }

    public RelationDataException(Object id, Throwable cause) {
        super(String.format(EXCEPTION_MESSAGE, id), cause);
    }
}
