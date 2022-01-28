package ru.otus.spring.library.repo;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootConfiguration
@ComponentScan("ru.otus.spring.library.mongock.changelog.test")
@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.spring.library.repo")
public class TestRepoConfiguration {
}
