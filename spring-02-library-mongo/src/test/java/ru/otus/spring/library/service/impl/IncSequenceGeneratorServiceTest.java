package ru.otus.spring.library.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.spring.library.domain.Genre;
import ru.otus.spring.library.service.SequenceGeneratorService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import({IncSequenceGeneratorService.class})
@DataMongoTest
@ExtendWith(SpringExtension.class)
class IncSequenceGeneratorServiceTest {

    @Autowired
    private SequenceGeneratorService<Integer> incSequenceGenerator;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    void generateSequence() {
        List<Integer> testCase = List.of(1, 2, 3, 4, 5);

        testCase.forEach(index -> assertThat(incSequenceGenerator.generateSequence(Genre.SEQUENCE_NAME))
                .isEqualTo(index));

    }
}
