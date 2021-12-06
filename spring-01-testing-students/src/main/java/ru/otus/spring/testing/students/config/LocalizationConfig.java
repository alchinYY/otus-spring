package ru.otus.spring.testing.students.config;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "l10n")
@Data
@NoArgsConstructor
public class LocalizationConfig {

    private String defaultTag;
    private Map<String, String> localMapping;

    @PostConstruct
    public void init(){
        Locale.setDefault(Locale.forLanguageTag(defaultTag));
        log.info("l10n prop: {}", this);
    }

}
