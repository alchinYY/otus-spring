package ru.otus.spring.testing.students.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.spring.testing.students.dao.DaoSimple;
import ru.otus.spring.testing.students.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserDaoSimple.class)
class UserDaoSimpleTest {

    @Autowired
    private DaoSimple<User> userDaoSimple;

    private User user;
    private User user2;

    @BeforeEach
    public void setUp(){
        user = new User("user_name");
        user2 = new User("2");

    }

    @Test
    void get_userExists() {
        userDaoSimple.create(user);

        assertThat(userDaoSimple.get())
                .isNotNull()
                .isEqualTo(user);
    }

    @Test
    void get_useNotExists() {
        assertThat(userDaoSimple.get())
                .isNull();
    }

    @Test
    void create_userExists() {
        userDaoSimple.create(user);
        userDaoSimple.create(user2);

        assertThat(userDaoSimple.get())
                .isEqualTo(user);
    }

    @Test
    void create_newExists() {
        userDaoSimple.create(user);

        assertThat(userDaoSimple.get())
                .isEqualTo(user);
    }
}