package ru.otus.spring.testing.students.util.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.testing.students.exception.FileCsvReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class CsvReaderClassPathResources<T> implements CsvReader<T> {

    private final String fileName;
    private final MappingStrategy<T> mappingStrategy;
    private final Class<T> aClass;

    public List<T> readData() {
        List<T> questionList;
        try (InputStream inputStream = getClass().getResourceAsStream(fileName)) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CsvToBean<T> cb = new CsvToBeanBuilder<T>(reader)
                    .withType(aClass)
                    .withMappingStrategy(mappingStrategy)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .build();
            questionList = cb.parse();
            reader.close();
            return questionList;
        } catch (Exception ex) {
            throw new FileCsvReadException(fileName);
        }
    }
}
