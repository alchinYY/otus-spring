package ru.otus.spring.testing.students.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.testing.students.dao.DaoSimple;
import ru.otus.spring.testing.students.domain.User;
import ru.otus.spring.testing.students.service.L10nMessageService;

import java.util.Optional;

import static ru.otus.spring.testing.students.config.MessageBundle.*;

@RequiredArgsConstructor
@Service
public class LoginEvent implements Event {

    private final L10nMessageService l10nMessageService;
    private final DaoSimple<User> userDaoSimple;

    @Override
    public String viewDescription() {
        return l10nMessageService.getMessage(MESSAGE_QUESTION_LOGIN);
    }

    @Override
    public String action(Object... args) {
        if (args.length == 2) {
            return Optional.ofNullable(userDaoSimple.get())
                    .map(u -> l10nMessageService.getMessage(MESSAGE_USER_EXISTS, u.getName()))
                    .orElseGet(() -> {
                        userDaoSimple.create(new User(args[0] + " " + args[1]));
                        return l10nMessageService.getMessage(MESSAGE_HELLO, userDaoSimple.get().getName());
                    });
        } else {
            return l10nMessageService.getMessage(MESSAGE_NOT_CORRECT_PARAMETER, "login");
        }
    }
}