package ru.otus.spring.testing.students;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.testing.students.service.MenuService;

public class Application {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");

        MenuService menuService = context.getBean(MenuService.class);
        menuService.createOneSession();
    }
}
