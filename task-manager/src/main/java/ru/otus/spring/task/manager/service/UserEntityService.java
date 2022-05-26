package ru.otus.spring.task.manager.service;

import ru.otus.spring.task.manager.model.UserEntity;

public interface UserEntityService {

    UserEntity create(UserEntity userEntity);

    UserEntity getById(Long id);

}
