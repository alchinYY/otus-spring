package ru.otus.spring.library.configuration;

import com.github.cloudyrock.spring.v5.EnableMongock;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongock
@EnableMongoRepositories(basePackages = "ru.otus.spring.library.repo")
@Configuration
@RequiredArgsConstructor
@EnableMongoAuditing
public class MongoConfig {
}
