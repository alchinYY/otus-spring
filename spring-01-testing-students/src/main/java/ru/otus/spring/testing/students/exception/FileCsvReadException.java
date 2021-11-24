package ru.otus.spring.testing.students.exception;

public class FileCsvReadException extends RuntimeException {

    public FileCsvReadException(String message) {
        super(message);
    }

}
