package ru.otus.spring.testing.students.events;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.testing.students.dao.DaoSimple;
import ru.otus.spring.testing.students.domain.User;
import ru.otus.spring.testing.students.service.L10nMessageService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static ru.otus.spring.testing.students.config.MessageBundle.*;

@SpringBootTest(classes = LoginEvent.class)
class LoginEventTest {

    private static final String VAR1 = "v1";
    private static final String VAR2 = "v2";
    private static final String VAR3 = "v3";

    @MockBean
    private L10nMessageService l10nMessageService;
    @MockBean
    private DaoSimple<User> userDaoSimple;

    @Autowired
    private LoginEvent loginEvent;


    @Test
    void viewDescription() {
        when(l10nMessageService.getMessage(MESSAGE_QUESTION_LOGIN))
                .thenReturn(MESSAGE_QUESTION_LOGIN);

        assertThat(loginEvent.viewDescription())
                .isEqualTo(MESSAGE_QUESTION_LOGIN);

        verify(l10nMessageService, times(1)).getMessage(anyString());
    }

    @Test
    void action_userExists() {
        when(l10nMessageService.getMessage(eq(MESSAGE_USER_EXISTS), anyString()))
                .thenReturn(MESSAGE_QUESTION_LOGIN);
        when(userDaoSimple.get()).thenReturn(new User(VAR1));

        assertThat(loginEvent.action(VAR1, VAR2))
                .contains(MESSAGE_QUESTION_LOGIN);

        verify(l10nMessageService, times(1)).getMessage(anyString(), anyString());
        verify(userDaoSimple, times(1)).get();
    }

    @Test
    void action_userNotExists() {
        when(l10nMessageService.getMessage(eq(MESSAGE_HELLO), any()))
                .thenReturn(MESSAGE_HELLO);
        when(userDaoSimple.get()).thenReturn(null, new User(VAR1 + " " + VAR2));

        assertThat(loginEvent.action(VAR1, VAR2))
                .contains(MESSAGE_HELLO);

        verify(l10nMessageService, times(1)).getMessage(anyString(), anyString());
        verify(userDaoSimple, times(2)).get();
    }

    @Test
    void action_argsNotCorrect() {

        when(l10nMessageService.getMessage(eq(MESSAGE_NOT_CORRECT_PARAMETER), any()))
                .thenReturn(MESSAGE_NOT_CORRECT_PARAMETER);


        assertThat(loginEvent.action(VAR1))
                .contains(MESSAGE_NOT_CORRECT_PARAMETER);

        assertThat(loginEvent.action())
                .contains(MESSAGE_NOT_CORRECT_PARAMETER);

        assertThat(loginEvent.action(VAR1, VAR2, VAR3))
                .contains(MESSAGE_NOT_CORRECT_PARAMETER);
        verify(l10nMessageService, times(3)).getMessage(anyString(), anyString());
    }
}
