package ru.otus.spring.library.exception;

public class GeneratorException extends LibraryException {
    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratorException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
