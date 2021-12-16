package ru.otus.spring.testing.students.exception;

public class FileCsvReadException extends RuntimeException {

    private static final String FILE_CSV_NOT_FOUND = "File %s with csv not found";

    public FileCsvReadException(String fileName) {
        super(String.format(FILE_CSV_NOT_FOUND, fileName));
    }

}
