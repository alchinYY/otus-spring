package ru.otus.spring.task.manager.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.otus.spring.task.manager.exception.UserNotFoundException;
import ru.otus.spring.task.manager.model.UserEntity;
import ru.otus.spring.task.manager.repo.UserRepo;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.liquibase.enabled=false")
@Import(UserService.class)
class UserServiceTest {

    private static final Long TEST_USER_ID_NOT_CORRECT = 100L;
    private static final String TEST_USER_LOGIN_NOT_CORRECT = "login";

    @MockBean
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Test
    void loadUserByUsername() {
        UserEntity userEntity = new UserEntity(1L, "login", "email");
        when(userRepo.findByLogin(any())).thenReturn(Optional.of(userEntity));

        assertThat(userService.loadUserByUsername(userEntity.getUsername())).isEqualTo(userEntity);
        verify(userRepo, times(1)).findByLogin(any());
    }

    @Test
    void create() {
        UserEntity userEntity = new UserEntity(null, "login", "email");
        UserEntity userEntitySaved = new UserEntity(1L, "login", "email");

        when(userRepo.save(any())).thenReturn(userEntitySaved);
        assertThat(userService.create(userEntity)).isEqualTo(userEntitySaved);
        verify(userRepo, times(1)).save(any());
    }

    @Test
    void getById() {
        UserEntity userEntity = new UserEntity(1L, "login", "email");
        when(userRepo.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        assertThat(userService.getById(userEntity.getId())).isEqualTo(userEntity);
        verify(userRepo, times(1)).findById(any());
    }

    @Test
    void getById_notFound() {
        when(userRepo.findById(TEST_USER_ID_NOT_CORRECT)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.getById(TEST_USER_ID_NOT_CORRECT));
        verify(userRepo, times(1)).findById(any());
    }

    @Test
    void loadUserByUsername_notFound() {
        when(userRepo.findByLogin(TEST_USER_LOGIN_NOT_CORRECT)).thenReturn(Optional.empty());

        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> userService.loadUserByUsername(TEST_USER_LOGIN_NOT_CORRECT));
        verify(userRepo, times(1)).findByLogin(any());
    }
}