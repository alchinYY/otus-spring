package ru.otus.spring.butterfly.config;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Min;

@Validated
@Configuration
@ConfigurationProperties(prefix = "caterpillar")
@Data
public class CaterpillarConfig {

    @Range(min = 1, max = 5)
    private long minSize;

    @Min(value = 5)
    private long maxSize;

    @Min(value = 1)
    private long transformTimeMs;

    @PostConstruct
    public void init() {
        System.out.println(this);
    }

}
