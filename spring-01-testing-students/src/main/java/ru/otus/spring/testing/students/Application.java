package ru.otus.spring.testing.students;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.testing.students.service.MenuService;

@ComponentScan
@Configuration
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);

        MenuService menuService = context.getBean(MenuService.class);
        menuService.createOneSession();
    }
}
