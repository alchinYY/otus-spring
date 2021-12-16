package ru.otus.spring.testing.students.dao.impl;

import org.springframework.stereotype.Repository;
import ru.otus.spring.testing.students.dao.DaoSimple;
import ru.otus.spring.testing.students.domain.User;

import java.util.Objects;

@Repository
public class UserDaoSimple implements DaoSimple<User> {

    private User user;

    @Override
    public User get() {
        return user;
    }

    @Override
    public void create(User user) {
        if(Objects.isNull(this.user)){
            this.user = user;
        }
    }
}
