package ru.otus.spring.task.manager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "ru.otus.spring.task.manager.repo", considerNestedRepositories = true)
public class JpaConfiguration {
}
