package ru.otus.spring.testing.students.service.impl;

import ru.otus.spring.testing.students.config.aop.ServiceWithAop;
import ru.otus.spring.testing.students.service.InOutService;

import java.util.Scanner;

@ServiceWithAop
public class ConsoleInOutService implements InOutService {

    private final Scanner scanner;

    public ConsoleInOutService(){
        scanner = new Scanner(System.in);
    }

    @Override
    public void print(String text) {
        System.out.print(text);
    }

    @Override
    public void println(String text) {
        System.out.println(text);
    }

    @Override
    public String read() {
        return scanner.nextLine();
    }
}
