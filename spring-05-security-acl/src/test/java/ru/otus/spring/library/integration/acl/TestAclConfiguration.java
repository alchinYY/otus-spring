package ru.otus.spring.library.integration.acl;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = { "ru.otus.spring.library.**" })
public class TestAclConfiguration {
}
