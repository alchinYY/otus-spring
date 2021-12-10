package ru.otus.spring.testing.students;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.otus.spring.testing.students.service.MenuService;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(Application.class, args);

		applicationContext.getBean(MenuService.class).createOneSession();
	}

}
