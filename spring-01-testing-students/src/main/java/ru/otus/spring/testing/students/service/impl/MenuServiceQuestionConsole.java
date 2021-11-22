package ru.otus.spring.testing.students.service.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.otus.spring.testing.students.dao.QuestionDao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.service.MenuService;

import java.util.Objects;
import java.util.Scanner;

@AllArgsConstructor
@NoArgsConstructor
public class MenuServiceQuestionConsole implements MenuService {

    private static final String MESSAGE_HELLO = "Добрый день, студент!\nНачинаем процесс тестирования";
    private static final String MESSAGE_PRINT_QUESTION = "Вопрос номер: %d\n%s\n";
    private static final String MESSAGE_OPTION = "%d - %s\n";
    private static final String MESSAGE_FIELD_TYPING = "> ";
    private static final String MESSAGE_CONGRATULATIONS = "Поздравляю, вы сдали.";
    private static final String MESSAGE_TEST_FAIL = "Вы не сдали тест! Пересдача.";
    private static final String MESSAGE_INPUT_ERROR = "Ошибка ввода. Вы ввели недопустимое значение. Это продолжится, пока не введете допустимое";

    private QuestionDao questionDao;
    private long minCorrectAnswer;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void createOneSession() {
        System.out.println(MESSAGE_HELLO);
        long correctAnswerCounter = questionDao.findAll().stream()
                .filter(question -> {
                    System.out.printf(MESSAGE_PRINT_QUESTION,
                            question.getId(),
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
            if (Objects.isNull(question.getAvailableInput()) || input.matches(question.getAvailableInput())) {
                return input.equalsIgnoreCase(question.getAnswer());
            } else {
                System.out.println(MESSAGE_INPUT_ERROR);
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

    private void destroy() {
        scanner.close();
    }


    boolean checkCorrectAnswers(long correctAnswerCounter) {
        return correctAnswerCounter < minCorrectAnswer;
    }

}
