package ru.otus.spring.testing.students.config;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.util.csv.CsvReaderClassPathResources;

@Configuration
public class CsvBeanConfig {

    @Bean
    public MappingStrategy<Question> csvQuestionDaoMapper() {
        MappingStrategy<Question> headerColumnNameMappingStrategy = new HeaderColumnNameMappingStrategy<>();
        headerColumnNameMappingStrategy.setType(Question.class);

        return headerColumnNameMappingStrategy;
    }

    @Bean
    public CsvReaderClassPathResources<Question> csvQuestionReader(
            @Value("${csv.file.name}") String csvFileName, MappingStrategy<Question> csvQuestionDaoMapper
    ) {
        return new CsvReaderClassPathResources<>(csvFileName, csvQuestionDaoMapper, Question.class);
    }

}
