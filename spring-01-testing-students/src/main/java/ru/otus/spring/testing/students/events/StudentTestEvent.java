package ru.otus.spring.testing.students.events;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.spring.testing.students.dao.DaoSimple;
import ru.otus.spring.testing.students.domain.User;
import ru.otus.spring.testing.students.service.L10nMessageService;
import ru.otus.spring.testing.students.service.MenuService;

import java.util.Optional;

import static ru.otus.spring.testing.students.config.MessageBundle.*;

@Service
@RequiredArgsConstructor
public class StudentTestEvent implements Event {


    private final L10nMessageService l10nMessageService;
    private final DaoSimple<User> userDaoSimple;
    private final MenuService menuService;

    @Override
    public String viewDescription() {
        return l10nMessageService.getMessage(MESSAGE_TEST_DESCRIPTION);
    }

    @Override
    public String action(Object... args) {
        return Optional.ofNullable(userDaoSimple.get())
                .map(user -> {
                            menuService.createOneSession();
                            return NEXT_LINE;
                        }
                )
                .orElse(l10nMessageService.getMessage(MESSAGE_NEED_LOGIN));
    }
}
