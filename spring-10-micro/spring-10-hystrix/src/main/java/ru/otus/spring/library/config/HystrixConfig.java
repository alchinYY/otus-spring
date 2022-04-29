package ru.otus.spring.library.config;

import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Configuration;

@EnableCircuitBreaker
@Configuration
public class HystrixConfig {
}
