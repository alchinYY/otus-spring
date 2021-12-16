package ru.otus.spring.testing.students.service.impl;

import org.springframework.beans.factory.annotation.Value;
import ru.otus.spring.testing.students.config.aop.ServiceWithAop;
import ru.otus.spring.testing.students.dao.Dao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;
import ru.otus.spring.testing.students.service.InOutService;
import ru.otus.spring.testing.students.service.L10nMessageService;
import ru.otus.spring.testing.students.service.MenuService;

import java.util.Objects;

import static ru.otus.spring.testing.students.config.MessageBundle.*;

@ServiceWithAop
public class MenuServiceQuestionConsole implements MenuService {

    private final long minCorrectAnswer;

    private final L10nMessageService l10nMessageService;
    private final Dao<Question> questionDao;
    private final InOutService inOutService;

    public MenuServiceQuestionConsole(
            @Value("${min.correct.answer}") long minCorrectAnswer,
            L10nMessageService l10nMessageService,
            Dao<Question> questionDao,
            InOutService inOutService
    ) {
        this.minCorrectAnswer = minCorrectAnswer;
        this.l10nMessageService = l10nMessageService;
        this.questionDao = questionDao;
        this.inOutService = inOutService;
    }

    @Override
    public void createOneSession() {
        printMessage(MESSAGE_START_TEST);
        long correctAnswerCounter = questionDao.findAll().stream()
                .filter(question -> {
                    printMessage(MESSAGE_PRINT_QUESTION, question.getId(), question.getType());
                    printMessage(question.getTestQuestion());
                    printOptions(question);
                    return checkAvailableInputWithOptions(question);
                }).count();
        if (checkCorrectAnswers(correctAnswerCounter)) {
            printMessage(MESSAGE_TEST_FAIL);
        } else {
            printMessage(MESSAGE_CONGRATULATIONS);
        }
    }

    boolean checkAvailableInputWithOptions(Question question) {
        while (true) {
            inOutService.print(MESSAGE_FIELD_TYPING);
            String input = inOutService.read();
            if (question.getType().equals(QuestionType.COMMON)) {
                return false;
            } else {
                if (Objects.isNull(question.getAvailableInput()) || input.matches(question.getAvailableInput())) {
                    return input.equalsIgnoreCase(question.getAnswer());
                } else {
                    printMessage(MESSAGE_INPUT_ERROR);
                }
            }
        }
    }

    private void printOptions(Question question) {
        if (Objects.nonNull(question.getOptions())) {
            question.getOptions().forEach(this::printMessage);
        }
    }

    private boolean checkCorrectAnswers(long correctAnswerCounter) {
        return correctAnswerCounter < minCorrectAnswer;
    }

    private void printMessage(String message, Object... args) {
        inOutService.println(l10nMessageService.getMessage(message, args));
    }

}
