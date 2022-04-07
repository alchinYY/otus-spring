package ru.otus.spring.butterfly.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import reactor.util.annotation.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Validated
@Configuration
@ConfigurationProperties(prefix = "butterfly")
@Data
public class ButterflyConfig {

    @NonNull
    @NotEmpty
    private Map<Long, String> configBySize;

    @Min(value = 1)
    private long transformTimeMs;

}
