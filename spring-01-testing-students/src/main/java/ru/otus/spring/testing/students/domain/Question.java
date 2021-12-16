package ru.otus.spring.testing.students.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Question {

    @CsvBindByName(required = true)
    private int id;

    @CsvBindByName(column = "test_question", required = true)
    private String testQuestion;

    @CsvBindByName(required = true)
    private QuestionType type;

    @CsvBindByName
    private String answer;

    @CsvBindAndSplitByName(elementType = String.class, splitOn = ";")
    private List<String> options;

    @CsvBindByName(column = "available_input")
    private String availableInput;
}

