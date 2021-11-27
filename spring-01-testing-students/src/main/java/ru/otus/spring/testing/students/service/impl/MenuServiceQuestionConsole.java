package ru.otus.spring.testing.students.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.spring.testing.students.dao.QuestionDao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;
import ru.otus.spring.testing.students.service.MenuService;

import javax.annotation.PreDestroy;
import java.util.Objects;
import java.util.Scanner;

@Service
public class MenuServiceQuestionConsole implements MenuService {

    private static final String MESSAGE_HELLO = "Hi, student!\nWe start testing";
    private static final String MESSAGE_PRINT_QUESTION = "Question: %s. Type: %s\n%s\n";
    private static final String MESSAGE_OPTION = "%d - %s\n";
    private static final String MESSAGE_FIELD_TYPING = "> ";
    private static final String MESSAGE_CONGRATULATIONS = "Congratulations!";
    private static final String MESSAGE_TEST_FAIL = "Test not passed.";
    private static final String MESSAGE_INPUT_ERROR = "Input error. You entered an invalid value. This will continue until you enter a valid";

    private final QuestionDao questionDao;
    private final long minCorrectAnswer;

    public MenuServiceQuestionConsole(@Value("${min.correct.answer}") long minCorrectAnswer, QuestionDao questionDao){
        this.minCorrectAnswer = minCorrectAnswer;
        this.questionDao = questionDao;
    }

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createOneSession() {
        System.out.println(MESSAGE_HELLO);
        long correctAnswerCounter = questionDao.findAll().stream()
                .filter(question -> {
                    System.out.printf(MESSAGE_PRINT_QUESTION,
                            question.getId(),
                            question.getType(),
                            question.getTestQuestion()
                    );
                    printOptions(question);
                    return checkAvailableInputWithOptions(question);
                }).count();
        if (checkCorrectAnswers(correctAnswerCounter)) {
            System.err.println(MESSAGE_TEST_FAIL);
        } else {
            System.out.println(MESSAGE_CONGRATULATIONS);
        }
    }

    boolean checkAvailableInputWithOptions(Question question) {
        while (true) {
            System.out.print(MESSAGE_FIELD_TYPING);
            String input = scanner.nextLine();
            if(question.getType().equals(QuestionType.COMMON)){
                return false;
            } else {
                if (Objects.isNull(question.getAvailableInput()) || input.matches(question.getAvailableInput())) {
                    return input.equalsIgnoreCase(question.getAnswer());
                } else {
                    System.out.println(MESSAGE_INPUT_ERROR);
                }
            }
        }
    }

    private void printOptions(Question question) {
        if (!question.getOptions().isEmpty()) {
            for (int i = 0; i < question.getOptions().size(); i++) {
                System.out.printf(MESSAGE_OPTION, i + 1, question.getOptions().get(i));
            }
        }
    }

    @PreDestroy
    private void destroy() {
        scanner.close();
    }


    boolean checkCorrectAnswers(long correctAnswerCounter) {
        return correctAnswerCounter < minCorrectAnswer;
    }

}
