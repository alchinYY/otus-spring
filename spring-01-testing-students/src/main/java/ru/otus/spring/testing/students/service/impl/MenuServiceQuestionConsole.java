package ru.otus.spring.testing.students.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.otus.spring.testing.students.config.LocalizationConfig;
import ru.otus.spring.testing.students.config.aop.ServiceWithAop;
import ru.otus.spring.testing.students.dao.Dao;
import ru.otus.spring.testing.students.domain.Question;
import ru.otus.spring.testing.students.domain.QuestionType;
import ru.otus.spring.testing.students.service.InOutService;
import ru.otus.spring.testing.students.service.L10nMessageService;
import ru.otus.spring.testing.students.service.MenuService;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@ServiceWithAop
@RequiredArgsConstructor
public class MenuServiceQuestionConsole implements MenuService {

    public static final String MESSAGE_HELLO = "common.hello";
    public static final String MESSAGE_GET_LOCALE = "common.read.locale";
    public static final String MESSAGE_GET_LOCALE_BTW = "common.read.locale.btw";
    public static final String MESSAGE_PRINT_QUESTION = "common.question_description";
    public static final String MESSAGE_FIELD_TYPING = "> ";
    public static final String MESSAGE_CONGRATULATIONS = "common.congratulation";
    public static final String MESSAGE_TEST_FAIL = "common.fail";
    public static final String MESSAGE_INPUT_ERROR = "common.input_error";

    @Value("${min.correct.answer}")
    private long minCorrectAnswer;

    private final L10nMessageService l10nMessageService;
    private final Dao<Question> questionDao;
    private final LocalizationConfig localizationConfig;
    private final InOutService inOutService;

    @Override
    public void createOneSession() {
        initLocale();
        printMessage(MESSAGE_HELLO);
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


    private void initLocale() {
        printMessage(MESSAGE_GET_LOCALE);
        localizationConfig.getLocalMapping()
                .forEach((k, v) -> printMessage(MESSAGE_GET_LOCALE_BTW, k, v));

        inOutService.print(MESSAGE_FIELD_TYPING);
        String input = inOutService.read();

        String localTag = localizationConfig.getLocalMapping().entrySet()
                .stream()
                .filter(entry -> entry.getKey().equals(input))
                .findAny()
                .map(Map.Entry::getValue)
                .orElse(localizationConfig.getDefaultTag());
        Locale.setDefault(Locale.forLanguageTag(localTag));
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

    boolean checkCorrectAnswers(long correctAnswerCounter) {
        return correctAnswerCounter < minCorrectAnswer;
    }

    private void printMessage(String message, Object... args) {
        inOutService.println(l10nMessageService.getMessage(message, args));
    }

}
