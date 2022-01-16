package ru.otus.spring.library.exception;

public class DomainExistException extends RuntimeException {

    public DomainExistException(String domain, Object object){
        super(String.format("existing entity - %s. %s", domain, object));
    }

}
