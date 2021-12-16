package ru.otus.spring.testing.students.events;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.testing.students.dao.DaoSimple;
import ru.otus.spring.testing.students.domain.User;
import ru.otus.spring.testing.students.service.L10nMessageService;
import ru.otus.spring.testing.students.service.MenuService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static ru.otus.spring.testing.students.config.MessageBundle.MESSAGE_NEED_LOGIN;
import static ru.otus.spring.testing.students.config.MessageBundle.MESSAGE_TEST_DESCRIPTION;

@SpringBootTest(classes = {StudentTestEvent.class})
class StudentTestEventTest {

    @MockBean
    private L10nMessageService l10nMessageService;
    @MockBean
    private DaoSimple<User> userDaoSimple;
    @MockBean
    private MenuService menuService;

    @Autowired
    private Event studentTestEvent;

    @Test
    void viewDescription() {
        when(l10nMessageService.getMessage(MESSAGE_TEST_DESCRIPTION))
                .thenReturn(MESSAGE_TEST_DESCRIPTION);

        assertThat(studentTestEvent.viewDescription())
                .isEqualTo(MESSAGE_TEST_DESCRIPTION);

        verify(l10nMessageService, times(1)).getMessage(anyString());
    }

    @Test
    void action_userNotLogin() {
        when(userDaoSimple.get()).thenReturn(null);
        when(l10nMessageService.getMessage(MESSAGE_NEED_LOGIN))
                .thenReturn(MESSAGE_NEED_LOGIN);

        assertThat(studentTestEvent.action())
                .contains(MESSAGE_NEED_LOGIN);

        verify(userDaoSimple, times(1)).get();
        verify(l10nMessageService, times(1)).getMessage(anyString());
    }

    @Test
    void action_correct() {
        User user = mock(User.class);

        when(userDaoSimple.get()).thenReturn(user);
        doNothing().when(menuService).createOneSession();

        studentTestEvent.action();

        verify(userDaoSimple, times(1)).get();
        verify(menuService, times(1)).createOneSession();
    }
}
