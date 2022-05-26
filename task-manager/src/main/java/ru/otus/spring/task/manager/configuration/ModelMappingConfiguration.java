package ru.otus.spring.task.manager.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.task.manager.utility.ObjectMapperUtils;

import java.util.List;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
@RequiredArgsConstructor
public class ModelMappingConfiguration {

    @Bean
    public ModelMapper modelMapper(
            List<PropertyMap<?, ?>> propertyMaps,
            List<Converter<?, ?>> converters
    ) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        propertyMaps.forEach(modelMapper::addMappings);
        converters.forEach(modelMapper::addConverter);
        return modelMapper;
    }

    @Bean
    public ObjectMapperUtils objectMapperUtils(ModelMapper modelMapper) {
        return new ObjectMapperUtils(modelMapper);
    }

}
