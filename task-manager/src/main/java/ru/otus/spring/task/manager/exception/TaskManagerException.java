package ru.otus.spring.task.manager.exception;


public abstract class TaskManagerException extends RuntimeException {

    public TaskManagerException(String message) {
        super(message);
    }

    public TaskManagerException(String message, Throwable cause) {
        super(message, cause);
    }

}
