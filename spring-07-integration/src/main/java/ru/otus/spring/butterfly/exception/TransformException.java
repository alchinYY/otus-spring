package ru.otus.spring.butterfly.exception;

public class TransformException extends ButterflyException {

    private static final String EXCEPTION_MESSAGE = "Transform failed for %s where index = %s";

    public TransformException(Class<?> aClass, long index) {
        super(String.format(EXCEPTION_MESSAGE, aClass.getName(), index));
    }

    public TransformException(String message) {
        super(message);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }
}
