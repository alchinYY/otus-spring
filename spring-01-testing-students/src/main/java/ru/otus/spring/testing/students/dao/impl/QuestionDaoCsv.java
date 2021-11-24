package ru.otus.spring.testing.students.dao.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.testing.students.dao.QuestionDao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.exception.FileCsvReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

    private final String csvFileWithData;
    private final MappingStrategy<Question> mappingStrategy;

    private List<Question> questionList;

    private void readDataFile() {
        try (InputStream inputStream = getClass().getResourceAsStream(csvFileWithData)) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CsvToBean<Question> cb = new CsvToBeanBuilder<Question>(reader)
                    .withType(Question.class)
                    .withMappingStrategy(mappingStrategy)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                    .build();
            questionList = cb.parse();
            reader.close();
        } catch (IOException ex) {
            throw new FileCsvReadException("File with csv not found");
        }
    }

    @Override
    public List<Question> findAll() {
        if (Objects.isNull(questionList)) {
            readDataFile();
        }
        return questionList;
    }

    @Override
    public Question getById(int id) {
        return questionList.get(id);
    }
}
