package ru.otus.spring.books.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.otus.spring.books.repo")
@EntityScan("ru.otus.spring")
public class JpaConfig {
}
