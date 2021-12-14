package ru.otus.spring.testing.students.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.Shell;
import org.springframework.shell.standard.commands.Quit;
import ru.otus.spring.testing.students.events.Event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.otus.spring.testing.students.config.MessageBundle.*;

@SpringBootTest
@DisplayName("Тест команд shell ")
class TestOfStudentsEventsCommandsTest {

    @MockBean(name = "localeEvent")
    private Event localeEvent;
    @MockBean(name = "loginEvent")
    private Event loginEvent;
    @MockBean(name = "studentTestEvent")
    private Event studentTestEvent;

    @Autowired
    private Shell shell;

    private static final String CMD_PATTERN_HELP = "%s %s";
    private static final String CMD_WITHOUT_KEY = "%s";
    private static final String CMD_PATTERN_ONE_ARG = "%s %s";
    private static final String OPT_HELP = "-h";

    private static final String LOGIN_LAST_NAME = "tseT";
    private static final String LOGIN_NAME = "Test";
    private static final String LOGIN_DESCRIPTION = "Login description";
    private static final String LOGIN_CMD_PATTERN = "%s %s %s";
    private static final String LOGIN_CMD_NOT_CORRECT_ARGS = "%s %s";
    private static final String LOGIN_CMD = "login";
    private static final String LOGIN_SHORT_CMD = "u";

    private static final String LOCALE_CMD = "locale";
    private static final String LOCALE_SHORT_CMD = "l";
    private static final String LOCALE_DESCRIPTION = "locale description";
    private static final String LOCALE_KEY = "key";

    private static final String STUDENT_TEST_CMD = "start";
    private static final String STUDENT_TEST_SHORT_CMD = "s";
    private static final String STUDENT_TEST_DESCRIPTION = "start test description";

    @Test
    @DisplayName(" должен вернуть help по команде start")
    void startTestingCmd_viewDescription() {

        when(studentTestEvent.viewDescription())
                .thenReturn(STUDENT_TEST_DESCRIPTION);

        String res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_HELP, STUDENT_TEST_CMD, OPT_HELP));
        assertThat(res).isEqualTo(STUDENT_TEST_DESCRIPTION);

        res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_HELP, STUDENT_TEST_SHORT_CMD, OPT_HELP));
        assertThat(res).isEqualTo(STUDENT_TEST_DESCRIPTION);
    }

    @Test
    @DisplayName(" должен выполнить команду start")
    void startTestingCmd_startTest() {

        when(studentTestEvent.action())
                .thenReturn(MESSAGE_SUCCESS);

        String res = (String) shell.evaluate(() -> String.format(CMD_WITHOUT_KEY, STUDENT_TEST_CMD));
        assertThat(res).isEqualTo(MESSAGE_SUCCESS);

        res = (String) shell.evaluate(() -> String.format(CMD_WITHOUT_KEY, STUDENT_TEST_SHORT_CMD));
        assertThat(res).isEqualTo(MESSAGE_SUCCESS);
    }

    @Test
    @DisplayName(" должен вернуть help по команде locale")
    void setLocaleCmd_viewDescription() {
        when(localeEvent.viewDescription())
                .thenReturn(LOCALE_DESCRIPTION);

        String res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_HELP, LOCALE_CMD, OPT_HELP));
        assertThat(res).isEqualTo(LOCALE_DESCRIPTION);

        res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_HELP, LOCALE_SHORT_CMD, OPT_HELP));
        assertThat(res).isEqualTo(LOCALE_DESCRIPTION);

    }

    @Test
    @DisplayName(" должен установить новое значение locale")
    void setLocaleCmd_setNewLocale() {
        when(localeEvent.action(LOCALE_KEY))
                .thenReturn(MESSAGE_SUCCESS);

        String res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_ONE_ARG, LOCALE_CMD, LOCALE_KEY));
        assertThat(res).isEqualTo(MESSAGE_SUCCESS);

        res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_ONE_ARG, LOCALE_SHORT_CMD, LOCALE_KEY));
        assertThat(res).isEqualTo(MESSAGE_SUCCESS);

        verify(localeEvent, times(2)).action(anyString());
    }

    @Test
    @DisplayName(" должен вернуть help по команде login")
    void setLoginCmd_showDescription() {
        when(loginEvent.viewDescription())
                .thenReturn(LOGIN_DESCRIPTION);

        String res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_HELP, LOGIN_CMD, OPT_HELP));
        assertThat(res).isEqualTo(LOGIN_DESCRIPTION);

        res = (String) shell.evaluate(() -> String.format(CMD_PATTERN_HELP, LOGIN_SHORT_CMD, OPT_HELP));
        assertThat(res).isEqualTo(LOGIN_DESCRIPTION);
    }

    @Test
    @DisplayName(" должен установить новое имя пользователя")
    void setLoginCmd_setLogin() {
        when(loginEvent.action(anyString(), anyString()))
                .thenReturn(MESSAGE_HELLO);

        String res = (String) shell.evaluate(() -> String.format(LOGIN_CMD_PATTERN, LOGIN_CMD, LOGIN_NAME, LOGIN_LAST_NAME));
        assertThat(res).isEqualTo(MESSAGE_HELLO);

        res = (String) shell.evaluate(() -> String.format(LOGIN_CMD_PATTERN, LOGIN_SHORT_CMD, LOGIN_NAME, LOGIN_LAST_NAME));
        assertThat(res).isEqualTo(MESSAGE_HELLO);

        verify(loginEvent, times(2)).action(anyString(), anyString());
    }

    @Test
    @DisplayName(" должен вывести предупреждение, пользователь уже существует")
    void setLoginCmd_existUser() {
        when(loginEvent.action(anyString(), anyString()))
                .thenReturn(MESSAGE_USER_EXISTS);

        String res = (String) shell.evaluate(() -> String.format(LOGIN_CMD_PATTERN, LOGIN_CMD, LOGIN_NAME, LOGIN_LAST_NAME));
        assertThat(res).isEqualTo(MESSAGE_USER_EXISTS);

        res = (String) shell.evaluate(() -> String.format(LOGIN_CMD_PATTERN, LOGIN_SHORT_CMD, LOGIN_NAME, LOGIN_LAST_NAME));
        assertThat(res).isEqualTo(MESSAGE_USER_EXISTS);

        verify(loginEvent, times(2)).action(anyString(), anyString());
    }

    @Test
    @DisplayName(" должен вывести предупреждение, что некорректное количество аргументов")
    void setLoginCmd_notCorrectArgs() {
        when(loginEvent.action(anyString(), eq(null)))
                .thenReturn(MESSAGE_NOT_CORRECT_PARAMETER);

        String res = (String) shell.evaluate(() -> String.format(LOGIN_CMD_NOT_CORRECT_ARGS, LOGIN_CMD, LOGIN_NAME));
        assertThat(res).isEqualTo(MESSAGE_NOT_CORRECT_PARAMETER);

        res = (String) shell.evaluate(() -> String.format(LOGIN_CMD_NOT_CORRECT_ARGS, LOGIN_SHORT_CMD, LOGIN_NAME));
        assertThat(res).isEqualTo(MESSAGE_NOT_CORRECT_PARAMETER);

        verify(loginEvent, times(2)).action(anyString(), eq(null));
    }
}