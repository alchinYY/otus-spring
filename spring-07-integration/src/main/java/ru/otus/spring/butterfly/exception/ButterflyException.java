package ru.otus.spring.butterfly.exception;

public class ButterflyException extends RuntimeException {
    public ButterflyException(String message) {
        super(message);
    }

    public ButterflyException(String message, Throwable cause) {
        super(message, cause);
    }

}
