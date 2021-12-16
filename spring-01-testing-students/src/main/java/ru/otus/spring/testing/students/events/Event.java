package ru.otus.spring.testing.students.events;

public interface Event {

    String viewDescription();

    String action(Object ... args);
}
