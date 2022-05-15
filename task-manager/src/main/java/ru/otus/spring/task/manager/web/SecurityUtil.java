package ru.otus.spring.task.manager.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.otus.spring.task.manager.model.UserEntity;

@Component
public class SecurityUtil {

    public UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
