package ru.otus.spring.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "ru.otus.spring.library.repository")
@Configuration
public class MongoConfig {
}
