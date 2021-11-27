package ru.otus.spring.testing.students.util.csv;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.exception.FileCsvReadException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DisplayName("Test for csv-readers")
class CsvReaderClassPathResourcesTest {

    private static final String TEST_CSV_FILE_FROM_RESOURCES = "/test.csv";
    private static final String FILE_CSV_NOT_FOUND = "not_found.csv";

    private static final long TEST_VALUE_MAX_QUESTION = 5;

    @Test
    void readData() {
        CsvReaderClassPathResources<Question> classPathResourcesCsvReader = new CsvReaderClassPathResources<>(
                TEST_CSV_FILE_FROM_RESOURCES,
                csvQuestionDaoMapper(),
                Question.class);
        assertThat(classPathResourcesCsvReader.readData().size())
                .isEqualTo(TEST_VALUE_MAX_QUESTION);
    }

    @Test
    void readData_expectedFileCsvReadException() {
        CsvReaderClassPathResources<Question> classPathResourcesCsvReader = new CsvReaderClassPathResources<>(
                FILE_CSV_NOT_FOUND,
                csvQuestionDaoMapper(),
                Question.class);
        assertThatExceptionOfType(FileCsvReadException.class)
                .isThrownBy(classPathResourcesCsvReader::readData);
    }

    private MappingStrategy<Question> csvQuestionDaoMapper() {
        MappingStrategy<Question> headerColumnNameMappingStrategy = new HeaderColumnNameMappingStrategy<>();
        headerColumnNameMappingStrategy.setType(Question.class);
        return headerColumnNameMappingStrategy;
    }
}