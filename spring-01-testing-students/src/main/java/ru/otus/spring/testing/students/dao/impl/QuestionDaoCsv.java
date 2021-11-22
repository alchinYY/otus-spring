package ru.otus.spring.testing.students.dao.impl;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import lombok.RequiredArgsConstructor;
import ru.otus.spring.testing.students.dao.QuestionDao;
import ru.otus.spring.testing.students.domain.Question;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class QuestionDaoCsv implements QuestionDao {

    private final String csvFileWithData;

    private List<Question> questionList;

    private void readDataFile() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream(csvFileWithData);
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);

        HeaderColumnNameMappingStrategy<Question> ms = new HeaderColumnNameMappingStrategy<>();
        ms.setType(Question.class);

        CsvToBean<Question> cb = new CsvToBeanBuilder<Question>(reader)
                .withType(Question.class)
                .withMappingStrategy(ms)
                .withFieldAsNull(CSVReaderNullFieldIndicator.BOTH)
                .build();
        questionList = cb.parse();
        reader.close();
    }

    @Override
    public List<Question> findAll() {
        return questionList;
    }

    @Override
    public Question getById(int id) {
        return questionList.get(id);
    }
}
