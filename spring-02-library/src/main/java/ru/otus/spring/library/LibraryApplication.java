package ru.otus.spring.library;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLException;


@SpringBootApplication
@EnableJpaRepositories
public class LibraryApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(LibraryApplication.class, args);
		Console.main(args);
	}
}
