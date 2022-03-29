package ru.otus.spring.library.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    private final List<PropertyMap<?, ?>> converters;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
//        Optional.ofNullable(converters)
//                .ifPresent(c -> c.forEach(modelMapper::addMappings));
        return modelMapper;
    }

}
